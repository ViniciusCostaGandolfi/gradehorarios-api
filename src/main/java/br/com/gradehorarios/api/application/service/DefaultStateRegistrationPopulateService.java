package br.com.gradehorarios.api.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gradehorarios.api.domain.entity.college.Address;
import br.com.gradehorarios.api.domain.entity.college.AreaType;
import br.com.gradehorarios.api.domain.entity.college.DependencyAdministrationType;
import br.com.gradehorarios.api.domain.entity.college.StateRegistration;
import br.com.gradehorarios.api.domain.repository.AddressRepository;
import br.com.gradehorarios.api.domain.repository.StateRegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class DefaultStateRegistrationPopulateService {

    @Autowired
    private StateRegistrationRepository stateRegistrationRepository;

    @Autowired
    private AddressRepository addressRepository;

    public void populateDatabase(String jsonFilePath) throws IOException {
        if (stateRegistrationRepository.count() > 0) {
            System.out.println("Banco já populado com as inscrições estaduais. Nenhuma ação necessária.");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

        for (JsonNode node : rootNode) {
            System.err.println(node.get("code"));
            StateRegistration registration = new StateRegistration();
            registration.setCode(node.get("code").asInt());
            registration.setName(node.get("name").asText());
            registration.setEmail(node.get("email").asText());
            registration.setPhone(node.get("phone").asText());
            registration.setLocalization(Enum.valueOf(AreaType.class, node.get("localization").asText()));
            registration.setDependencyAdministration(Enum.valueOf(DependencyAdministrationType.class, node.get("dependencyAdministration").asText()));

            JsonNode addressNode = node.get("address");
            if (addressNode != null) {
                Address address = new Address();
                address.setCountry(addressNode.get("country").asText());
                address.setState(addressNode.get("state").asText());
                address.setCity(addressNode.get("city").asText());
                address.setNeighborhood(addressNode.get("neighborhood").asText());
                address.setPostalCode(addressNode.get("postalCode").asText());
                address.setStreetName(addressNode.get("streetName").asText());
                address.setStreetNumber(addressNode.get("streetNumber").asText());
                address.setFormattedAddress(addressNode.get("formattedAddress").asText());
                address.setComplement(addressNode.get("complement").asText());

                if (addressNode.get("latitude").isNumber()) {
                    address.setLatitude(addressNode.get("latitude").decimalValue());
                }

                if (addressNode.get("longitude").isNumber()) {
                    address.setLongitude(addressNode.get("longitude").decimalValue());
                }
                
                this.addressRepository.save(address);

                registration.setAddress(address);
            }

            this.stateRegistrationRepository.save(registration);
        }

    }
}
