package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.UserMapper;
import edu.ut.sales.sales_analyst.model.dtos.events.PasswordChangedEvent;
import edu.ut.sales.sales_analyst.model.dtos.requests.*;
import edu.ut.sales.sales_analyst.model.dtos.responses.StatsResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.Role;
import edu.ut.sales.sales_analyst.producer.EventProducer;
import edu.ut.sales.sales_analyst.repositories.TokenRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.security.CustomUserDetails;
import edu.ut.sales.sales_analyst.services.impl.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService implements IUserService {

    private final UserRepo userRepo;

    private final TokenRepo tokenRepo;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtils jwtTokenUtils;

    private final AuthenticationManager authenticationManager;

    private final EventProducer  eventProducer;


    public UserService(UserRepo userRepo, TokenRepo tokenRepo, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, EventProducer eventProducer) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.eventProducer = eventProducer;
    }

    @Override
    public UserDetailResponse createUser(UserCreateRequest user) {
        User existUser = userRepo.findByEmail(user.getEmail());
        if (existUser != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.CUSTOMER);
        userRepo.save(newUser);
        return userMapper.toUserDetailDTO(newUser);
    }

    @Override
    public UserDetailResponse getUser(String userId) {
        User existUser = userRepo.findByUserId(userId);
        if (existUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userMapper.toUserDetailDTO(existUser);
    }

    @Override
    public Page<UserResponse> getAllUsersWithFilter(Pageable pageable, Boolean isActive) {
        Page<User> users = userRepo.findAllByIsActiveNullable(isActive, pageable);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.LIST_USER_NOT_FOUND);
        }
        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserDetailResponse updateUser(String userId, UserCreateRequest customer) {
        User existUser = userRepo.findByUserId(userId);
        if (existUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        existUser.setUserName(customer.getUserName());
        existUser.setEmail(customer.getEmail());
        existUser.setPhone(customer.getPhone());
        userRepo.save(existUser);
        return userMapper.toUserDetailDTO(existUser);
    }


    @Override
    public Boolean blockUser(String userId) {
        User user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getIsActive()) {
            user.setIsActive(false);
            userRepo.save(user);
        }

        return true;
    }

    @Override
    public Boolean unBlockUser(String userId) {
        User user = userRepo.findByUserId(userId);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (!user.getIsActive()) {
            user.setIsActive(true);
            userRepo.save(user);
        }

        return true;
    }

    @Override
    public String login(LoginRequest accountLoginRequest) {
        User existingUser = userRepo.findByEmail(accountLoginRequest.getEmail());
        if (existingUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(accountLoginRequest.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong email or password");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountLoginRequest.getEmail(),
                        accountLoginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);

        UserDetails userDetails = new CustomUserDetails(existingUser);
        return jwtTokenUtils.generateToken(userDetails);
    }

    @Override
    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User currentUser = getCurrentUser();
        User user = userRepo.findByUserId(currentUser.getUserId());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(resetPasswordRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong old password");
        }
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new BadCredentialsException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepo.save(user);
        log.info("[PW] about to send event for user={}", user.getUserId());
        log.info("[PW] eventProducer bean = {}", eventProducer.getClass().getName());

        eventProducer.sendPasswordChangedEvent(
                new PasswordChangedEvent(user.getUserId(), LocalDateTime.now())
        );

        log.info("[PW] sent() invoked");
        return true;
    }

    @Override
    public Boolean forgetPassword(ForgotPasswordRequest forgotPasswordsRequest) {
        User user = userRepo.findByEmail(forgotPasswordsRequest.getEmail());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        if (!forgotPasswordsRequest.getPassword().equals(forgotPasswordsRequest.getConfirmPassword())) {
            throw new BadCredentialsException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(forgotPasswordsRequest.getPassword()));
        userRepo.save(user);
        return true;
    }

    @Override
    public User getUserDetailsFromToken(String token) {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        }
        String email = jwtTokenUtils.extractEmail(token);
        User account = userRepo.findByEmail(email);
        if (account != null) {
            return account;
        } else {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public User getUserFromRefreshToken(String refreshToken) {
        Token tokenEntity = tokenRepo.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (tokenEntity.getRefreshExpirationDate().isBefore(LocalDateTime.now()) || tokenEntity.isRevoked()) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return tokenEntity.getUser();
    }

    @Override
    public UserResponse getUserFromEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getProfile() {
        User user = getCurrentUser();
        return userMapper.toUserResponse(user);
    }


    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepo.findByEmail(username);
            if (user == null) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }
            return user;
        } else {
            throw new IllegalStateException("User not authenticated!");
        }
    }
}