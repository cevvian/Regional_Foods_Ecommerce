package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create new user", description = "API create new user's information")
    @PostMapping()
    public ResponseAPI<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        try {
            UserResponse userResponse = userService.createUser(userCreateRequest);
            return new ResponseAPI<>("Create user successfully", HttpStatus.CREATED, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Get all user", description = "API get all user")
    @GetMapping()
    public ResponseAPI<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserResponse> userResponseList = userService.getAllUsers(pageable);
            return new ResponseAPI<>("Get all users", HttpStatus.OK, userResponseList);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<UserResponse> getUserById(@PathVariable String id) {
        try {
            UserResponse userResponse = userService.getUser(id);
            return new ResponseAPI<>("Get user successfully", HttpStatus.OK, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<UserResponse> updateUser(@PathVariable String id, @RequestBody UserCreateRequest userCreateRequest) {
        try {
            UserResponse userResponse = userService.updateUser(id, userCreateRequest);
            return new ResponseAPI<>("Update user successfully", HttpStatus.OK, userResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteUser(@PathVariable String id) {
        try {
            Boolean response = userService.deleteUser(id);
            if (response) {
                return new ResponseAPI<>("Delete user successfully", HttpStatus.OK, null);
            }
            return new ResponseAPI<>("Delete user failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
