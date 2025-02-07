package br.com.gradehorarios.api.domain.entity.college;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "state_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class StateRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer code;

    private String name;

    private String email;

    private String phone;
    
    @OneToOne
    @JoinColumn(name = "addressId")
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private AreaType localization;

    @Enumerated(value = EnumType.STRING)
    private DependencyAdministrationType dependencyAdministration;
}
