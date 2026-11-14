package com.elevata.marketplace.address.domain;

import com.elevata.marketplace.address.AddressResponse;
import com.elevata.marketplace.security.CurrentUser;
import com.elevata.marketplace.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AddressServiceImpl implements AddressService {
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final CurrentUser currentUser;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CurrentUser currentUser) {
        this.addressRepository = addressRepository;
        this.currentUser = currentUser;
    }

    @Override
    @Transactional
    public AddressResponse save(AddressRequest addressRequest) {
        Long appUserId = currentUser.id();

        if (addressRepository.countByAppUserId(appUserId) == 5) {
            throw new AddressLimitReachedException("Address limit reached");
        }

        if (!Country.INDIA.name().equalsIgnoreCase(addressRequest.country())) {
            throw new InvalidCountryException("Invalid country " + addressRequest.country());
        }

        //Check the default address
        if (addressRequest.isDefault()) {

        }
        Address address = AddressMapper.toEntity(addressRequest, appUserId);

        address = addressRepository.save(address);
        return AddressMapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressResponse update(Long addressId, AddressUpdateRequest updateRequest) {
        Address address = addressRepository.findByAddressIdAndAppUserId(addressId, currentUser.id())
                .orElseThrow(() -> new AddressNotFoundException("Invalid addressId " + addressId));
        boolean isUpdated = false;

        try {
            if (!checkNull(updateRequest.street())) {
                address.setStreet(updateRequest.street());
                isUpdated = true;
            }

            if (!checkNull(updateRequest.society())) {
                address.setSociety(updateRequest.society());
                isUpdated = true;
            }

            if (!checkNull(updateRequest.townVillageCity())) {
                address.setTownVillageCity(updateRequest.townVillageCity());

                if (!checkNull(updateRequest.zipcode())) {
                    address.setZipcode(updateRequest.zipcode());
                } else {
                    throw new RuntimeException("Cannot update zipcode. Field required.");
                }
                isUpdated =  true;
            }

            if (updateRequest.isDefault() != null && updateRequest.isDefault()) {
                //What will happen if there isn't a default address?
                log.info("[Inside update default if]");
                handleDefaultAddress(address.getAppUserId());
                address.setDefault(true);
                isUpdated = true;
            }

            if (!isUpdated) {
                throw new RuntimeException("Cannot update. Please add at least 1 field.");
            }
        } catch (NullPointerException e) {
            log.info(e.getMessage());
            throw e;
        }

        address = addressRepository.save(address);
        log.info("[Address updated] From the Address service");
        return AddressMapper.toDto(address);
    }

    @Override
    public AddressResponse findById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Invalid addressId " + addressId));

        return AddressMapper.toDto(address);
    }

    @Override
    public Long getDefaultAddressByCustomerId(Long customerId) {
        return addressRepository.getDefaultAddressByCustomerId(customerId)
                .map(Address::getAddressId)
                .orElseThrow(() -> new AddressNotFoundException("Default address not found."));
    }

    private void handleDefaultAddress(Long appUserId) {
        addressRepository.getDefaultAddressByCustomerId(appUserId)
                .ifPresent(address -> {
                    address.setDefault(false);
                    addressRepository.save(address);
                    log.info("[Old default removed] From the Address service");
                });

        log.info("[Old default address doesn't exist] From the Address service");
    }

    private boolean checkNull(String value) throws NullPointerException {
        return value == null;
    }
}
