package br.com.gradehorarios.gradehorarios.application.service.solution;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.service.email.IEmailService;
import br.com.gradehorarios.gradehorarios.domain.entity.Solution;

@Service
public class SolutionNotificationService {

    @Autowired
    private IEmailService emailService;

    @Value("${frontend-url}")
    private String frontendUrl;

    public void notifySolutionProcessed(Solution solution) {
        String userEmail = solution.getUser().getEmail();
        String userName = solution.getUser().getName();
        String institutionName = solution.getInstitution().getName();
        
        String solutionUrl = String.format("%s/app/institutions/%d/solutions/%d",
            frontendUrl,
            solution.getInstitution().getId(),
            solution.getId()
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("institutionName", institutionName);
        variables.put("status", solution.getSolverStatus());
        variables.put("solutionUrl", solutionUrl);

        emailService.sendEmail(
            userEmail,
            "Sua grade de horários está pronta! - " + institutionName,
            "email/solution-processed",
            variables
        );
    }
}