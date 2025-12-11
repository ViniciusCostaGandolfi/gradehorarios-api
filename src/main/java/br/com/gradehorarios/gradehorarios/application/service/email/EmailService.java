package br.com.gradehorarios.gradehorarios.application.service.email;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openpdf.pdf.ITextRenderer; // Importação da OpenPDF 3.0
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class EmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private Environment env;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {

        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process(templateName, context);

        if (!env.acceptsProfiles(Profiles.of("prod"))) {
            logger.info("[MOCK EMAIL] Envio simulado para: {} | Assunto: {}", to, subject);
            generateAndSaveLocalPdf(to, htmlContent);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email enviado com sucesso para: {}", to);

        } catch (MessagingException e) {
            logger.error("Erro ao enviar email para: {}", to, e);
        }
    }

    private void generateAndSaveLocalPdf(String to, String htmlContent) {
        try {
            File dir = new File("mock_emails");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss"));
            String sanitizedTo = to.replaceAll("[^a-zA-Z0-9.-]", "_");
            String fileName = "email_" + sanitizedTo + "_" + timestamp + ".pdf";
            
            File file = new File(dir, fileName);

            Document doc = Jsoup.parse(htmlContent, "UTF-8");
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            String xhtml = doc.html();

            try (OutputStream os = new FileOutputStream(file)) {
                ITextRenderer renderer = new ITextRenderer();
                
                renderer.setDocumentFromString(xhtml);
                
                renderer.layout();
                
                renderer.createPDF(os);
            }

            logger.info("[MOCK EMAIL] PDF Salvo em: {}", file.getAbsolutePath());

        } catch (IOException e) {
            logger.error("Erro ao gerar PDF local do email", e);
        } catch (Exception e) {
            logger.error("Erro genérico na renderização do PDF", e);
        }
    }
}