package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.LoginRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    UserDetailResponse createUser(UserCreateRequest user);

    UserDetailResponse getUser(String userId);

    Page<UserDetailResponse> getAllUsersWithFilter(Pageable pageable, Boolean isActive);

    UserDetailResponse updateUser(String userId, UserCreateRequest customer);

    Boolean blockUser(String userId);

    Boolean unBlockUser(String userId);

    String login(LoginRequest accountLoginRequest) throws Exception;

    User getUserDetailsFromToken(String token);

    User getUserFromRefreshToken(String refreshToken);
}
