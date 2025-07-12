package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.CustomerMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.CustomerCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;
import edu.ut.sales.sales_analyst.model.entities.Customer;
import edu.ut.sales.sales_analyst.repositories.CustomerRepo;
import edu.ut.sales.sales_analyst.services.impl.ICustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepo customerRepo;

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepo customerRepo, CustomerMapper customerMapper) {
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest customerCreateRequest) {
        if (customerRepo.findByEmail(customerCreateRequest.getEmail()) != null) {
            throw new AppException(ErrorCode.CUSTOMER_ALREADY_EXISTS);
        }

        Customer customer = new Customer();
        customer.setCustomerName(customerCreateRequest.getCustomerName());
        customer.setEmail(customerCreateRequest.getEmail());
        customer.setPhone(customerCreateRequest.getPhone());
        customer.setAddress(customerCreateRequest.getAddress());
        customerRepo.save(customer);

        return customerMapper.toCustomerDTO(customer);
    }

    @Override
    public CustomerResponse getCustomer(String customerId) {
        Customer customer = customerRepo.findByCustomerId(customerId);
        if (customer == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customerMapper.toCustomerDTO(customer);
    }

    @Override
    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> customerList = customerRepo.findAll(pageable);
        if (customerList.isEmpty()) {
            throw new AppException(ErrorCode.LIST_CUSTOMER_NOT_FOUND);
        }
        return customerList.map(customerMapper::toCustomerDTO);
    }

    @Override
    public CustomerResponse updateCustomer(String customerId, CustomerCreateRequest customer) {
        Customer customerToUpdate = customerRepo.findByCustomerId(customerId);
        if (customerToUpdate == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        customerToUpdate.setCustomerName(customer.getCustomerName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setPhone(customer.getPhone());
        customerToUpdate.setAddress(customer.getAddress());
        customerRepo.save(customerToUpdate);

        return customerMapper.toCustomerDTO(customerToUpdate);
    }

    @Override
    public Boolean deleteCustomer(String customerId) {
        Customer customerToDelete = customerRepo.findByCustomerId(customerId);
        if (customerToDelete == null) {
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        customerRepo.delete(customerToDelete);
        return true;
    }
}
