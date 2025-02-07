package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.StateRegistration;
import br.com.gradehorarios.api.domain.entity.college.AreaType;
import br.com.gradehorarios.api.domain.entity.college.DependencyAdministrationType;

public record StateRegistrationDto(
    Integer id,
    Integer code,
    String name,
    String email,
    String phone,
    AddressDto address,
    AreaType localization,
    DependencyAdministrationType dependencyAdministration
) {

    public StateRegistrationDto(StateRegistration stateRegistration) {
        this(
            stateRegistration.getId(),
            stateRegistration.getCode(),
            stateRegistration.getName(),
            stateRegistration.getEmail(),
            stateRegistration.getPhone(),
            new AddressDto(stateRegistration.getAddress()),
            stateRegistration.getLocalization(),
            stateRegistration.getDependencyAdministration()
        );
    }
}
