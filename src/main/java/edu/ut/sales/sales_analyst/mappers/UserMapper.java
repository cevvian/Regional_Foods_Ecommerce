package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailResponse toUserDetailDTO(User user);

    UserResponse toUserResponse(User user);

    User toUser(UserDetailResponse userDetailResponse);
}
