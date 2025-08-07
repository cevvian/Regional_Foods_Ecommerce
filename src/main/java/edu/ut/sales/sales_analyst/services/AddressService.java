package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.mappers.AddressMapper;
import edu.ut.sales.sales_analyst.mappers.UserMapper;
import edu.ut.sales.sales_analyst.model.dtos.requests.AddressRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.AddressResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.UserDetailResponse;
import edu.ut.sales.sales_analyst.model.entities.Address;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.AddressRepo;
import edu.ut.sales.sales_analyst.services.impl.IAddressService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AddressService implements IAddressService {

    private final AddressRepo addressRepo;

    private final AddressMapper addressMapper;

    private final UserMapper userMapper;

    private final UserService userService;

    public AddressService(AddressRepo addressRepo, AddressMapper addressMapper, UserMapper userMapper, UserService userService) {
        this.addressRepo = addressRepo;
        this.addressMapper = addressMapper;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Override
    public AddressResponse getAddress(String addressId) {
        Address existAddress = addressRepo.findByAddressId(addressId);
        if (existAddress == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressMapper.toAddressResponse(existAddress);
    }

    @Override
    public Page<AddressResponse> getAddressesByUserId(Pageable pageable, String userId) {
        UserDetailResponse user =  userService.getUser(userId);
        Page<Address> addresses = addressRepo.findAllByUser_UserId(pageable, userId);
        if (addresses.isEmpty()) {
            throw new AppException(ErrorCode.ADDRESS_LIST_EMPTY);
        }
        return addresses.map(addressMapper::toAddressResponse);
    }

    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest addressRequest) {
        boolean exists = addressRepo.existsByUser_UserIdAndAddressLineAndProvinceAndPhone(
                addressRequest.getUserId(),
                addressRequest.getAddressLine(),
                addressRequest.getProvince(),
                addressRequest.getPhone()
        );

        if (exists) {
            throw new AppException(ErrorCode.ADDRESS_ALREADY_EXISTS);
        }

        UserDetailResponse userDetailResponse = userService.getUser(addressRequest.getUserId());
        User user = userMapper.toUser(userDetailResponse);

        // Nếu request là default thì update các địa chỉ khác thành false trước
        if (addressRequest.getIsDefault()) {
            addressRepo.updateDefaultAddressToFalse(user.getUserId());
        }

        Address address = new Address();
        address.setAddressLine(addressRequest.getAddressLine());
        address.setPhone(addressRequest.getPhone());
        address.setProvince(addressRequest.getProvince());
        address.setUser(user);

        address.setIsDefault(addressRequest.getIsDefault());

        Address savedAddress = addressRepo.save(address);

        return addressMapper.toAddressResponse(savedAddress);
    }

    @Override
    public AddressResponse updateAddress(String addressId, AddressRequest addressRequest) {
        Address existAddress = addressRepo.findByAddressId(addressId);
        if (existAddress == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        boolean exists = addressRepo.existsByUser_UserIdAndAddressLineAndProvinceAndPhone(
                addressRequest.getUserId(),
                addressRequest.getAddressLine(),
                addressRequest.getProvince(),
                addressRequest.getPhone()
        );

        if (exists &&
                (!existAddress.getAddressLine().equals(addressRequest.getAddressLine())
                        || !existAddress.getProvince().equals(addressRequest.getProvince())
                        || !existAddress.getPhone().equals(addressRequest.getPhone()))) {
            throw new AppException(ErrorCode.ADDRESS_ALREADY_EXISTS);
        }

        existAddress.setAddressLine(addressRequest.getAddressLine());
        existAddress.setProvince(addressRequest.getProvince());
        existAddress.setPhone(addressRequest.getPhone());

        addressRepo.save(existAddress);
        return addressMapper.toAddressResponse(existAddress);
    }


    @Override
    public AddressResponse changeDefaultAddress(String addressId) {
        Address address = addressRepo.findByAddressId(addressId);
        if (address == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        addressRepo.updateDefaultAddressToFalse(address.getUser().getUserId());

        address.setIsDefault(true);
        addressRepo.save(address);

        return addressMapper.toAddressResponse(address);
    }


    @Override
    public Boolean deleteAddress(String addressId) {
        Address address = addressRepo.findByAddressId(addressId);
        if (address == null) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        if (address.getIsDefault()) {
            throw new AppException(ErrorCode.CANNOT_DELETE_DEFAULT_ADDRESS);
        }

        addressRepo.delete(address);
        return true;
    }

}