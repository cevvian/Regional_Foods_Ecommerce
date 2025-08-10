package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ResetPasswordRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.UserCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserResponse;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(
        name = "Users",
        description = "APIs for managing users: registration, retrieving details, updating information, blocking and unblocking accounts"
)
public class UserController {

    UserService userService;

    @Operation(summary = "Create new user")
    @PostMapping("/register")
    public ResponseEntity<ResponseAPI<UserDetailResponse>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        UserDetailResponse userResponse = userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseAPI<>("User created successfully", HttpStatus.CREATED, userResponse));
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<ResponseAPI<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> userPage = userService.getAllUsersWithFilter(pageable, isActive);

        PageMeta meta = PageMeta.builder()
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .build();

        return ResponseEntity.ok(new ResponseAPI<>("Users fetched successfully", HttpStatus.OK, userPage.getContent(), meta));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<UserDetailResponse>> getUserById(@PathVariable String id) {
        UserDetailResponse userResponse = userService.getUser(id);
        return ResponseEntity.ok(new ResponseAPI<>("User retrieved successfully", HttpStatus.OK, userResponse));
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<UserDetailResponse>> updateUser(
            @PathVariable String id,
            @RequestBody UserCreateRequest userCreateRequest) {
        UserDetailResponse updatedUser = userService.updateUser(id, userCreateRequest);
        return ResponseEntity.ok(new ResponseAPI<>("User updated successfully", HttpStatus.OK, updatedUser));
    }

    @Operation(summary = "Block user")
    @PatchMapping("/{id}/block")
    public ResponseEntity<ResponseAPI<Void>> blockUser(@PathVariable String id) {
        boolean success = userService.blockUser(id);
        if (!success) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI<>("User block failed", HttpStatus.BAD_REQUEST, null));
        }
        return ResponseEntity
                .ok(new ResponseAPI<>("User blocked successfully", HttpStatus.OK, null));
    }

    @Operation(summary = "Unblock user")
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<ResponseAPI<Void>> unblockUser(@PathVariable String id) {
        boolean success = userService.unBlockUser(id);
        if (!success) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI<>("User unblock failed", HttpStatus.BAD_REQUEST, null));
        }
        return ResponseEntity
                .ok(new ResponseAPI<>("User unblocked successfully", HttpStatus.OK, null));
    }

    @Operation(summary = "Reset user password")
    @PostMapping("/reset-password/{id}")
    public ResponseEntity<ResponseAPI<Boolean>> resetPassword(
            @PathVariable String id,
            @RequestBody ResetPasswordRequest request) {

        Boolean result = userService.resetPassword(id, request);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI<>("Password reset failed", HttpStatus.BAD_REQUEST, false));
        }

        return ResponseEntity
                .ok(new ResponseAPI<>("Password reset successfully", HttpStatus.OK, true));
    }

}
