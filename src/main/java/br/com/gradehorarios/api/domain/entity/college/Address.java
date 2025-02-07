package br.com.gradehorarios.api.domain.entity.college;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String country;
    private String state;
    private String city;
    private String neighborhood;
    private String postalCode;
    private String streetName;
    private String streetNumber;
    private String formattedAddress;
    private String complement;
    @Column(scale = 6, precision = 9)
    private BigDecimal latitude;
    @Column(scale = 6, precision = 9)
    private BigDecimal longitude;

}
