package br.com.gradehorarios.api.infra.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.gradehorarios.api.application.service.DefaultStateRegistrationPopulateService;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DefaultStateRegistrationPopulateService populateService;


    @Override
    public void run(String... args) throws Exception {
        String jsonFilePath = "src/main/resources/db/populate/defaultStateRegistration.json";
        populateService.populateDatabase(jsonFilePath);
        System.out.println("Database populated successfully!");
    }
}
