package com.elevata.marketplace.address.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@DynamicInsert
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String street;
    private String society;
    private String townVillageCity;
    private String state;

    @Enumerated(EnumType.STRING)
    private Country country;
    private String zipcode;
    private boolean isDefault;
    private Long appUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Address() {
    }

    public Address(String street, String society, String townVillageCity, String state, Country country, String zipcode, boolean isDefault, Long appUserId) {
        this.street = street;
        this.society = society;
        this.townVillageCity = townVillageCity;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.isDefault = isDefault;
        this.appUserId = appUserId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getTownVillageCity() {
        return townVillageCity;
    }

    public void setTownVillageCity(String townVillageCity) {
        this.townVillageCity = townVillageCity;
    }

    public String getState() {
        return state;
    }

    public Country getCountry() {
        return country;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }
}
