package com.elevata.marketplace.address.domain;

import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final CurrentUser currentUser;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CurrentUser currentUser) {
        this.addressRepository = addressRepository;
        this.currentUser = currentUser;
    }

    @Override
    public AddressResponse save(AddressRequest addressRequest) {
        Long appUserId = currentUser.id();

        if (addressRepository.countByAppUserId(appUserId) == 5) {
            throw new AddressLimitReachedException("Address limit reached");
        }

        if (!Country.INDIA.name().equalsIgnoreCase(addressRequest.country())) {
            throw new InvalidCountryException("Invalid country " + addressRequest.country());
        }

        Address address = AddressMapper.convertToEntity(addressRequest, appUserId);

        address = addressRepository.save(address);
        return AddressMapper.convertToDto(address);
    }

    @Override
    public AddressResponse update(Long addressId, String street, String society, String townVillageCity, String zipcode) {
        Address address = addressRepository.findByAddressIdAndAppUserId(addressId, currentUser.id())
                .orElseThrow(() -> new AddressNotFoundException("Invalid addressId " + addressId));
        int counter = 0;

        try {

            if (!checkNull(street)) {
                address.setStreet(street);
                counter++;
            }

            if (!checkNull(society)) {
                address.setSociety(society);
                counter++;
            }

            if (!checkNull(townVillageCity)) {
                address.setTownVillageCity(townVillageCity);

                if (!checkNull(zipcode)) {
                    address.setZipcode(zipcode);
                    counter++;
                } else {
                    throw new RuntimeException("Cannot update zipcode. townVillageCity field required.");
                }
                counter++;
            }

            if (counter == 0) {
                throw new RuntimeException("Cannot update. Please add at least 1 field.");
            }


        } catch (NullPointerException e) {
            log.info(e.getMessage());
            throw e;
        }

        address = addressRepository.save(address);
        return AddressMapper.convertToDto(address);
    }

    private boolean checkNull(String value) throws NullPointerException {
        return value == null;
    }
}
