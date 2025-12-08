package br.com.gradehorarios.gradehorarios.application.service.solution;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.application.service.email.IEmailService;
import br.com.gradehorarios.gradehorarios.application.service.storage.StorageService;
import br.com.gradehorarios.gradehorarios.domain.entity.Solution;

@Service
public class SolutionNotificationService {

    @Autowired
    private IEmailService emailService;

    @Autowired
    private StorageService storageService;

    @Value("${frontend-url:http://localhost:3000}")
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

        String teacherFullUrl = solution.getTeacherOutputPath() != null 
            ? storageService.getPublicUrl(solution.getTeacherOutputPath()) 
            : null;

        String classroomFullUrl = solution.getClassroomOutputPath() != null 
            ? storageService.getPublicUrl(solution.getClassroomOutputPath()) 
            : null;

        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("institutionName", institutionName);
        variables.put("status", solution.getSolverStatus());
        
        variables.put("teacherUrl", teacherFullUrl);
        variables.put("classroomUrl", classroomFullUrl);
        
        variables.put("solutionUrl", solutionUrl);
        variables.put("duration", (solution.getDurationMillis() != null ? solution.getDurationMillis() : 0) + "ms");

        emailService.sendEmail(
            userEmail,
            "Sua grade de horários está pronta! - " + institutionName,
            "email/solution-processed",
            variables
        );
    }
}