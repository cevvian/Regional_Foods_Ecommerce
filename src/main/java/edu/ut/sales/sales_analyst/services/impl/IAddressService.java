package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.AddressRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAddressService {
    AddressResponse getAddress(String addressId);
    Page<AddressResponse> getAddressesByUserId(Pageable pageable, String userId);
    AddressResponse createAddress(AddressRequest addressRequest);
    AddressResponse updateAddress(String addressId, AddressRequest addressRequest);
    AddressResponse changeDefaultAddress(String addressId);
    Boolean deleteAddress(String addressId);
}
