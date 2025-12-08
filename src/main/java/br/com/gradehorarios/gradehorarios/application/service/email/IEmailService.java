package br.com.gradehorarios.gradehorarios.application.service.email;

import java.util.Map;

public interface IEmailService {
        public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);

}
