package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.CustomerCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;

import java.util.List;

public interface ICustomerService {

    CustomerResponse createCustomer(CustomerCreateRequest customer);

    CustomerResponse getCustomer(String customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(String customerId, CustomerCreateRequest customer);

    Boolean deleteCustomer(String customerId);
}
