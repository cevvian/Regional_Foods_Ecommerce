package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    UserService userService;

    @Operation(summary = "Create new user", description = "API create new user's information")
    @PostMapping("/register")
    public ResponseAPI<UserDetailResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        try {
            UserDetailResponse userResponse = userService.createUser(userCreateRequest);
            return new ResponseAPI<>("Create user successfully", HttpStatus.CREATED, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Get all user", description = "API get all user with pagination (active or not)")
    @GetMapping()
    public ResponseAPI<Page<UserDetailResponse>> getAllUsers(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserDetailResponse> userResponseList = userService.getAllUsersWithFilter(pageable, isActive);
            return new ResponseAPI<>("Get all users", HttpStatus.OK, userResponseList);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<UserDetailResponse> getUserById(@PathVariable String id) {
        try {
            UserDetailResponse userResponse = userService.getUser(id);
            return new ResponseAPI<>("Get user successfully", HttpStatus.OK, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<UserDetailResponse> updateUser(@PathVariable String id, @RequestBody UserCreateRequest userCreateRequest) {
        try {
            UserDetailResponse userResponse = userService.updateUser(id, userCreateRequest);
            return new ResponseAPI<>("Update user successfully", HttpStatus.OK, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> blockUser(@PathVariable String id) {
        try {
            Boolean response = userService.blockUser(id);
            if (response) {
                return new ResponseAPI<>("Delete user successfully", HttpStatus.OK, null);
            }
            return new ResponseAPI<>("Delete user failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PatchMapping("/{id}")
    public ResponseAPI<Boolean> unblockUser(@PathVariable String id) {
        try {
            Boolean response = userService.unBlockUser(id);
            if (response) {
                return new ResponseAPI<>("Unblock user successfully", HttpStatus.OK, null);
            }
            return new ResponseAPI<>("Unblock user failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
