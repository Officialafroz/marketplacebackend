package com.elevata.marketplace.address.web.controller;

import com.elevata.marketplace.address.AddressService;
import com.elevata.marketplace.address.domain.AddressRequest;
import com.elevata.marketplace.address.domain.AddressResponse;
import com.elevata.marketplace.validations.ValidZipcode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public AddressResponse save(@Valid @RequestBody AddressRequest addressRequest) {
        return addressService.save(addressRequest);
    }

    @PatchMapping("{addressId}")
    public AddressResponse update(@PathVariable Long addressId,
                                  @RequestParam(required = false) String street,
                                  @RequestParam(required = false) String society,
                                  @RequestParam(required = false) String townVillageCity,
                                  @ValidZipcode @RequestParam(required = false) String zipcode
    ) {
        return addressService.update(addressId, street, society, townVillageCity, zipcode);
    }
}
