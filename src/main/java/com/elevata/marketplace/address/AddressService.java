package com.elevata.marketplace.address;

import com.elevata.marketplace.address.domain.AddressRequest;
import com.elevata.marketplace.address.domain.AddressUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    AddressResponse save(AddressRequest addressRequest);

    AddressResponse update(Long addressId, AddressUpdateRequest updateRequest);

    AddressResponse findById(Long addressId);

    Long getDefaultAddressByCustomerId(Long customerId);
}
