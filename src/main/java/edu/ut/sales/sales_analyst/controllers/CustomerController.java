package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.model.dtos.requests.CustomerCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create new customer", description = "API create new customer's information")
    @PostMapping()
    public ResponseAPI<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest) {
        try {
            CustomerResponse customerResponse = customerService.createCustomer(customerCreateRequest);
            return new ResponseAPI<>("Create customer successfully", HttpStatus.CREATED, customerResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Get all customer", description = "API get all customer")
    @GetMapping()
    public ResponseAPI<Page<CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<CustomerResponse> customerResponseList = customerService.getAllCustomers(pageable);
            return new ResponseAPI<>("Get all customers", HttpStatus.OK, customerResponseList);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/{id}")
    public ResponseAPI<CustomerResponse> getCustomerById(@PathVariable String id) {
        try {
            CustomerResponse customerResponse = customerService.getCustomer(id);
            return new ResponseAPI<>("Get customer successfully", HttpStatus.OK, customerResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/{id}")
    public ResponseAPI<CustomerResponse> updateCustomer(@PathVariable String id, @RequestBody CustomerCreateRequest customerCreateRequest) {
        try {
            CustomerResponse customerResponse = customerService.updateCustomer(id, customerCreateRequest);
            return new ResponseAPI<>("Update customer successfully", HttpStatus.OK, customerResponse);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseAPI<Boolean> deleteCustomer(@PathVariable String id) {
        try {
            Boolean response = customerService.deleteCustomer(id);
            if (response) {
                return new ResponseAPI<>("Delete customer successfully", HttpStatus.OK, null);
            }
            return new ResponseAPI<>("Delete customer failed", HttpStatus.INTERNAL_SERVER_ERROR, null);
        } catch (AppException e) {
            return new ResponseAPI<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}
