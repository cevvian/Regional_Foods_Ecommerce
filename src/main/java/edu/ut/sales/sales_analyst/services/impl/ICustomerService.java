package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.CustomerCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest customer);

    CustomerResponse getCustomer(String customerId);

    Page<CustomerResponse> getAllCustomers(Pageable pageable);

    CustomerResponse updateCustomer(String customerId, CustomerCreateRequest customer);

    Boolean deleteCustomer(String customerId);
}
