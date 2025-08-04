package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.AddressRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.AddressResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.AddressService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Addresses", description = "APIs for managing user addresses")
public class AddressController {

    AddressService addressService;

    @Operation(summary = "Get address by ID", description = "Retrieve address details by address ID")
    @GetMapping("/{addressId}")
    public ResponseAPI<AddressResponse> getAddress(@PathVariable String addressId) {
        try {
            AddressResponse response = addressService.getAddress(addressId);
            return new ResponseAPI<>("Get address successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Get all addresses of a user", description = "Retrieve all addresses of a specific user with pagination")
    @GetMapping("/user/{userId}")
    public ResponseAPI<Page<AddressResponse>> getAddressesByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AddressResponse> response = addressService.getAddressesByUserId(pageable, userId);
            return new ResponseAPI<>("Get addresses successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Create a new address", description = "Add a new address for the user")
    @PostMapping
    public ResponseAPI<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        try {
            AddressResponse response = addressService.createAddress(request);
            return new ResponseAPI<>("Create address successfully", HttpStatus.CREATED, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Update address", description = "Update an existing address by address ID")
    @PutMapping("/{addressId}")
    public ResponseAPI<AddressResponse> updateAddress(
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        try {
            AddressResponse response = addressService.updateAddress(addressId, request);
            return new ResponseAPI<>("Update address successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Change default address", description = "Set a specific address as the default address for the user")
    @PatchMapping("/{addressId}/default")
    public ResponseAPI<AddressResponse> changeDefaultAddress(@PathVariable String addressId) {
        try {
            AddressResponse response = addressService.changeDefaultAddress(addressId);
            return new ResponseAPI<>("Change default address successfully", HttpStatus.OK, response);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Delete address", description = "Delete an address by ID (cannot delete default address)")
    @DeleteMapping("/{addressId}")
    public ResponseAPI<Boolean> deleteAddress(@PathVariable String addressId) {
        try {
            Boolean deleted = addressService.deleteAddress(addressId);
            return new ResponseAPI<>("Delete address successfully", HttpStatus.OK, deleted);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
    }
}

