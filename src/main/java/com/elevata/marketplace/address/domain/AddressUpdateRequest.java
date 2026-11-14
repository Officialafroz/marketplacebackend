package com.elevata.marketplace.address.domain;

import com.elevata.marketplace.validations.ValidZipcode;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

public record AddressUpdateRequest(
        @Nullable
        String street,
        @Nullable
        String society,
        @Nullable
        String townVillageCity,
        @Nullable @ValidZipcode
        String zipcode,
        @Nullable @JsonProperty("isDefault")
        Boolean isDefault
) {
}
