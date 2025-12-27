package br.com.gradehorarios.gradehorarios.timetable.domain.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.shared.domain.service.EmailService;
import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.Solution;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;

@Service
public class SolutionNotificationService {

    private final EmailService emailService;
    private final FileStorageService storageService;
    private final String frontendUrl;

    public SolutionNotificationService(
            EmailService emailService,
            FileStorageService storageService,
            @Value("${frontend-url:http://localhost:3000}") 
            String frontendUrl
    ) {
        this.emailService = emailService;
        this.storageService = storageService;
        this.frontendUrl = frontendUrl;
    }

    public void notifySolutionProcessed(Solution solution) {
        String userEmail = solution.getUser().getEmail();
        String userName = solution.getUser().getName();
        String institutionName = solution.getInstitution().getName();
        
        // O resto do código permanece idêntico...
        String solutionUrl = String.format("%s/admin/instituicoes/%d/solucoes/%d",
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

        var statusPresentation = this.determineStatusPresentation(solution.getSolverStatus());
        variables.put("statusText", statusPresentation.text());
        variables.put("statusClass", statusPresentation.cssClass());
        
        variables.put("teacherUrl", teacherFullUrl);
        variables.put("classroomUrl", classroomFullUrl);
        
        variables.put("solutionUrl", solutionUrl);
        variables.put("duration", (solution.getDurationMillis() != null ? solution.getDurationMillis() / 1000 : 0L) + "s");

        emailService.sendEmail(
            userEmail,
            "Sua grade de horários está pronta! - " + institutionName,
            "email/solution-processed",
            variables
        );
    }

    private StatusPresentation determineStatusPresentation(SolverStatus status) {
        String text;
        String cssClass;

        if (status == null) {
            text = "DESCONHECIDO";
            cssClass = "status-warning";
        } else {
            switch (status) {
                case FEASIBLE, OPTIMAL -> {
                    text = "SUCESSO";
                    cssClass = "status-success";
                }
                case INFEASIBLE -> {
                    text = "INVIÁVEL";
                    cssClass = "status-error";
                }
                case ERROR -> {
                    text = "ERRO";
                    cssClass = "status-error";
                }
                default -> {
                    text = status.name();
                    cssClass = "status-warning";
                }
            }
        }

        return new StatusPresentation(text, cssClass);
    }

    private record StatusPresentation(String text, String cssClass) {}
}