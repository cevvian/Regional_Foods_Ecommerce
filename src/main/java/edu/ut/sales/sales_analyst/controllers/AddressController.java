package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.AddressRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.AddressResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.PageMeta;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Addresses", description = "APIs for managing user addresses")
public class AddressController {

    AddressService addressService;

    @Operation(summary = "Get address by ID")
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<AddressResponse> getAddress(@PathVariable String addressId) {
        AddressResponse response = addressService.getAddress(addressId);
        return new ResponseAPI<>("Get address successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Get all addresses of a user")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<List<AddressResponse>> getAddressesByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AddressResponse> addressPage = addressService.getAddressesByUserId(pageable, userId);

        PageMeta meta = PageMeta.builder()
                .page(addressPage.getNumber())
                .size(addressPage.getSize())
                .totalElements(addressPage.getTotalElements())
                .totalPages(addressPage.getTotalPages())
                .last(addressPage.isLast())
                .build();

        return new ResponseAPI<>("Get addresses successfully", HttpStatus.OK, addressPage.getContent(), meta);
    }

    @Operation(summary = "Create a new address")
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        AddressResponse response = addressService.createAddress(request);
        return new ResponseAPI<>("Create address successfully", HttpStatus.CREATED, response);
    }

    @Operation(summary = "Update address")
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<AddressResponse> updateAddress(
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request
    ) {
        AddressResponse response = addressService.updateAddress(addressId, request);
        return new ResponseAPI<>("Update address successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Change default address")
    @PatchMapping("/{addressId}/default")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<AddressResponse> changeDefaultAddress(@PathVariable String addressId) {
        AddressResponse response = addressService.changeDefaultAddress(addressId);
        return new ResponseAPI<>("Change default address successfully", HttpStatus.OK, response);
    }

    @Operation(summary = "Delete address")
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseAPI<Boolean> deleteAddress(@PathVariable String addressId) {
        Boolean deleted = addressService.deleteAddress(addressId);
        return new ResponseAPI<>("Delete address successfully", HttpStatus.OK, deleted);
    }
}
