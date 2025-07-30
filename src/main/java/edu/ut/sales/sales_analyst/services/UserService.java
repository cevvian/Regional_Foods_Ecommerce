package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.components.JwtTokenUtils;
import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.UserMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.LoginRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.Role;
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

    public UserService(UserRepo userRepo, TokenRepo tokenRepo, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
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
        return userMapper.toUserDTO(newUser);
    }

    @Override
    public UserDetailResponse getUser(String userId) {
        User existUser = userRepo.findByUserId(userId);
        if (existUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userMapper.toUserDTO(existUser);
    }

    @Override
    public Page<UserDetailResponse> getAllUsersWithFilter(Pageable pageable, Boolean isActive) {
        Page<User> users = userRepo.findAllByIsActiveNullable(isActive, pageable);
        if (users.isEmpty()) {
            throw new AppException(ErrorCode.LIST_USER_NOT_FOUND);
        }
        return users.map(userMapper::toUserDTO);
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
        return userMapper.toUserDTO(existUser);
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
    public String login(LoginRequest accountLoginRequest) throws Exception {
        try {
            User existingUser = userRepo.findByEmail(accountLoginRequest.getEmail());
            if (existingUser == null) {
                throw new AppException(ErrorCode.USER_NOT_FOUND);
            }

            if (!passwordEncoder.matches(accountLoginRequest.getPassword(), existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong email or password");
            }

            // Auth check
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(accountLoginRequest.getEmail(),
                            accountLoginRequest.getPassword());
            authenticationManager.authenticate(authenticationToken);

            // Load CustomUserDetails and generate token
//            UserDetails userDetails = (UserDetails) userRepo.findByEmail(accountLoginRequest.getEmail());
            UserDetails userDetails = new CustomUserDetails(existingUser);
            return jwtTokenUtils.generateToken(userDetails);

        } catch (Exception e) {
            log.error("Login failed for user {}: {}", accountLoginRequest.getEmail(), e.getMessage());
            throw new Exception(e.getMessage(), e);
        }
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
}