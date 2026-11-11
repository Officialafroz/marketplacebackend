package com.elevata.marketplace.address.domain;

class AddressMapper {

    static AddressResponse toDto(Address entity) {
        return new AddressResponse(entity.getAddressId(),
                entity.getStreet(),
                entity.getSociety(),
                entity.getTownVillageCity(),
                entity.getState(),
                entity.getCountry().name(),
                entity.getZipcode(),
                entity.getAppUserId());
    }

    static Address toEntity(AddressRequest request, Long appUserId) {
        return new Address(request.street(),
                request.society(),
                request.townVillageCity(),
                request.state(),
                Country.valueOf(request.country().toUpperCase()),
                request.zipcode(),
                appUserId);
    }
}
