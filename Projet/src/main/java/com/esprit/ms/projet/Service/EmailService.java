package com.esprit.ms.projet.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendProjectCreationEmail(String to, String userName, String projectTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🎉 New Project Created: " + projectTitle);

            String htmlContent = buildProjectCreatedEmail(userName, projectTitle, LocalDate.now().toString());
            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildProjectCreatedEmail(String userName, String projectTitle, String date) {
        return "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2E86C1;'>Hello " + userName + ",</h2>"
                + "<p>Your new project <strong>" + projectTitle + "</strong> was created on <em>" + date + "</em>.</p>"
                + "<p>Thanks for using our platform! 🚀</p>"
                + "<hr><p style='font-size: 12px; color: gray;'>This is an automatic message – do not reply.</p>"
                + "</body></html>";
    }
}

