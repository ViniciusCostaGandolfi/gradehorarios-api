package br.com.gradehorarios.api.application.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.gradehorarios.api.application.dto.StateRegistrationDto;
import br.com.gradehorarios.api.application.service.StateRegistrationService;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping(ApiVersion.VERSION + "/state-registration")
public class StateRegistrationController {

    @Autowired
    private StateRegistrationService stateRegistrationService;

    @GetMapping("/default-code/{code}")
    private StateRegistrationDto getById(@PathVariable Integer code) {
        return new StateRegistrationDto(stateRegistrationService.findByCode(code));
    }
}
