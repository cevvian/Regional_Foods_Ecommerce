package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.CustomerMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.CustomerCreateRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;
import edu.ut.sales.sales_analyst.model.entities.Customer;
import edu.ut.sales.sales_analyst.repositories.CustomerRepo;
import edu.ut.sales.sales_analyst.services.impl.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CustomerCreateRequest customerCreateRequest) {
        if (customerRepo.findByEmail(customerCreateRequest.getEmail()) != null) {
            throw new AppException(ErrorCode.CUSTOMER_EMAIL_NOT_FOUND);
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
    public List<CustomerResponse> getAllCustomers() {
        List <Customer> customerList = customerRepo.findAll();
        if (customerList.isEmpty()) {
            throw new AppException(ErrorCode.lIST_CUSTOMER_NOT_FOUND);
        }
        return customerMapper.toCustomerDTOList(customerList);
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
