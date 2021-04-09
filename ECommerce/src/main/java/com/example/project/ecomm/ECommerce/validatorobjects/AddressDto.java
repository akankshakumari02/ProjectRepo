package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Data;

@Data
public class AddressDto {
    private String addressLine;
    private String city;
    private String state;
    private String country;
    private String label;
    private int zipCode;

    @Override
    public String toString() {
        return "AddressDto{" +
                "addressLine='" + addressLine + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", label='" + label + '\'' +
                ", zipCode=" + zipCode +
                '}';
    }
}
