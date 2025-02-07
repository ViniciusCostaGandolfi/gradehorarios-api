package br.com.gradehorarios.api.application.service;

import br.com.gradehorarios.api.domain.entity.college.User;
import br.com.gradehorarios.api.domain.repository.UserRepository;
import br.com.gradehorarios.api.infra.security.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${api.security.allowed.origin}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String email) throws JsonProcessingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String resetToken = tokenService.generateToken(user).accessToken();

        String resetUrl = frontendUrl + "/recuperar-senha/" + resetToken;

        try {
            sendEmail(email, resetUrl);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        String email = tokenService.getSubject(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private void sendEmail(String email, String resetUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        String htmlContent = templateEngine.process("password-reset-email", context);

        helper.setTo(email);
        helper.setSubject("Redefinição de Senha");
        helper.setText(htmlContent, true);
        helper.setFrom("contato.rotafood@gmail.com");
        mailSender.send(message);
    }
}
