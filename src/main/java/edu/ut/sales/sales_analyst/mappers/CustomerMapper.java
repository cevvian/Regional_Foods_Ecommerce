package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.CustomerResponse;
import edu.ut.sales.sales_analyst.model.entities.Customer;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toCustomerDTO(Customer customer);
}
