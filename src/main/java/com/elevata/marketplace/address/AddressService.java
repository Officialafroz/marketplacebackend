package com.elevata.marketplace.address;

import com.elevata.marketplace.address.domain.AddressRequest;
import com.elevata.marketplace.address.domain.AddressUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    ResponseEntity<AddressResponse> save(AddressRequest addressRequest);

    ResponseEntity<AddressResponse> update(Long addressId, AddressUpdateRequest updateRequest);

    AddressResponse findById(Long addressId);

    Long getDefaultAddressByCustomerId(Long customerId);
}
