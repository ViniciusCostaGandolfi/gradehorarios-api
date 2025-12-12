package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.shared.domain.service.EmailService;
import br.com.gradehorarios.gradehorarios.shared.infra.storage.S3FileStorageService;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.Solution;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;

@Service
public class SolutionNotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private S3FileStorageService storageService;

    @Value("${frontend-url:http://localhost:3000}")
    private String frontendUrl;

    public void notifySolutionProcessed(Solution solution) {
        String userEmail = solution.getUser().getEmail();
        String userName = solution.getUser().getName();
        String institutionName = solution.getInstitution().getName();
        
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
        variables.put("duration", (solution.getDurationMillis() != null ? solution.getDurationMillis() : 0) + "ms");

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
                case FEASIBLE:
                case OPTIMAL:
                    text = "SUCESSO";
                    cssClass = "status-success";
                    break;
                
                case INFEASIBLE:
                    text = "INVIÁVEL";
                    cssClass = "status-error";
                    break;
                
                case ERROR:
                    text = "ERRO";
                    cssClass = "status-error";
                    break;
                
                default:
                    text = status.name();
                    cssClass = "status-warning";
                    break;
            }
        }

        return new StatusPresentation(text, cssClass);
    }


    private record StatusPresentation(String text, String cssClass) {}
}