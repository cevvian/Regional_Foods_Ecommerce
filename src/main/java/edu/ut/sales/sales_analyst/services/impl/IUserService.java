package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    UserResponse createUser(UserCreateRequest customer);

    UserResponse getUser(String userId);

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(String userId, UserCreateRequest customer);

    Boolean deleteUser(String userId);
}
