package br.com.gradehorarios.api.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.com.gradehorarios.api.domain.entity.college.Address;

public record AddressDto(
    Integer id,

    @NotBlank
    String country,
    
    @NotBlank
    String state,
    
    @NotBlank
    String city,
    
    @NotBlank
    String neighborhood,
    
    @NotBlank
    String postalCode,
    
    @NotBlank
    String streetName,

    @NotNull
    String streetNumber,
    
    String formattedAddress,
    
    String complement,

    BigDecimal latitude,
    
    BigDecimal longitude
) {

    public AddressDto(Address address) {
        this(
            address.getId(),
            address.getCountry(),
            address.getState(),
            address.getCity(),
            address.getNeighborhood(),
            address.getPostalCode(),
            address.getStreetName(),
            address.getStreetNumber(),
            address.getFormattedAddress(),
            address.getComplement(),
            address.getLatitude(),
            address.getLongitude()
        );
    }
}
