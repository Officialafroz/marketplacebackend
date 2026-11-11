package com.elevata.marketplace.address.domain;

public record AddressResponse(
        Long addressId,
        String street,
        String society,
        String townVillageCity,
        String State,
        String country,
        String zipcode,
        Long appUserId
) {
}
