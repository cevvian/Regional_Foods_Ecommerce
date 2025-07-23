package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.UserMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.Role;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.services.impl.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepo userRepo;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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
}