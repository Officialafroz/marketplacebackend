package com.elevata.marketplace.address.web.controller;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.address.AddressService;
import com.elevata.marketplace.address.domain.AddressRequest;
import com.elevata.marketplace.address.domain.AddressUpdateRequest;
import com.elevata.marketplace.validations.ValidZipcode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer/address")
class AddressController {

    private final AddressService addressService;

    @Autowired
    AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    AddressResponse save(@Valid @RequestBody AddressRequest addressRequest) {
        return addressService.save(addressRequest);
    }

    @PatchMapping("{addressId}")
    AddressResponse update(@PathVariable Long addressId,
                           @RequestBody AddressUpdateRequest updateRequest) {
        return addressService.update(addressId, updateRequest);
    }
}
