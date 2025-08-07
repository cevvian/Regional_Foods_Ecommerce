package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.AddressResponse;
import edu.ut.sales.sales_analyst.model.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "default", target = "defaultAddress")
    AddressResponse toAddressResponse(Address address);


}
