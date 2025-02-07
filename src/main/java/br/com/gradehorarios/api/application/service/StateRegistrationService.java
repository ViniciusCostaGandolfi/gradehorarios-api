package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.domain.entity.college.StateRegistration;
import br.com.gradehorarios.api.application.dto.StateRegistrationDto;
import br.com.gradehorarios.api.domain.entity.college.Address;

import br.com.gradehorarios.api.domain.repository.StateRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StateRegistrationService {

    @Autowired
    private StateRegistrationRepository stateRegistrationRepository;

    public StateRegistration getById(Integer id) {
        return stateRegistrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("State Registration não encontrado."));
    }

    @Transactional
    public StateRegistration updateOrCreate(StateRegistrationDto stateRegistrationDto) {
        StateRegistration stateRegistration = stateRegistrationRepository.findById(stateRegistrationDto.id()).orElse(new StateRegistration());
        
        stateRegistration.setCode(stateRegistrationDto.code());
        stateRegistration.setName(stateRegistrationDto.name());
        stateRegistration.setEmail(stateRegistrationDto.email());
        stateRegistration.setPhone(stateRegistrationDto.phone());
        stateRegistration.setLocalization(stateRegistrationDto.localization());
        stateRegistration.setDependencyAdministration(stateRegistrationDto.dependencyAdministration());

        Address address = stateRegistration.getAddress() != null ? stateRegistration.getAddress() : new Address();
        address.setCountry(stateRegistrationDto.address().country());
        address.setState(stateRegistrationDto.address().state());
        address.setCity(stateRegistrationDto.address().city());
        address.setNeighborhood(stateRegistrationDto.address().neighborhood());
        address.setPostalCode(stateRegistrationDto.address().postalCode());
        address.setStreetName(stateRegistrationDto.address().streetName());
        address.setStreetNumber(stateRegistrationDto.address().streetNumber());
        address.setFormattedAddress(stateRegistrationDto.address().formattedAddress());
        address.setComplement(stateRegistrationDto.address().complement());
        address.setLatitude(stateRegistrationDto.address().latitude());
        address.setLongitude(stateRegistrationDto.address().longitude());   
        
        stateRegistration.setAddress(address);

        return stateRegistrationRepository.save(stateRegistration);
    }

    public StateRegistration findByCode(Integer code) {
        return this.stateRegistrationRepository.findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Registro de estado com código " + code + " não encontrado"));

    }
}
