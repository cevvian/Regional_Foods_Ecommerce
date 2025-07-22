package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.services.impl.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {


    @Override
    public UserResponse createUser(UserCreateRequest customer) {
        return null;
    }

    @Override
    public UserResponse getUser(String userId) {
        return null;
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return null;
    }

    @Override
    public UserResponse updateUser(String userId, UserCreateRequest customer) {
        return null;
    }

    @Override
    public Boolean deleteUser(String userId) {
        return null;
    }
}