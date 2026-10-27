package com.elevata.marketplace.address;

import com.elevata.marketplace.address.domain.AddressRequest;
import com.elevata.marketplace.address.domain.AddressResponse;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    AddressResponse save(AddressRequest addressRequest);

    AddressResponse update(Long addressId, String street, String society, String townVillageCity, String zipcode);
}
