package com.elevata.marketplace.address.domain;

import com.elevata.marketplace.validations.ValidZipcode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull @NotBlank
        String street,
        @NotNull @NotBlank
        String society,
        @NotNull @NotBlank
        String townVillageCity,
        @NotNull @NotBlank
        String state,
        @NotNull @NotBlank
        String country,
        @NotNull @ValidZipcode
        String zipcode
) {
}
