package com.esprit.stage.Service;

import com.esprit.stage.Entities.InternshipOffer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class OfferEmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM = "eflexgym@gmail.com";
    private final String TO = "nour.benmna04@gmail.com"; // ✅ destinataire réel

    public void sendOfferCreatedEmail(InternshipOffer offer) {
        String subject = "Nouvelle offre de stage publiée !";
        String body = generateOfferHtml("Une nouvelle offre vient d’être publiée :", offer);
        sendHtmlEmail(TO, subject, body);
    }

    public void sendOfferUpdatedEmail(InternshipOffer offer) {
        String subject = " Offre de stage mise à jour";
        String body = generateOfferHtml("🛠️ Une offre a été modifiée :", offer);
        sendHtmlEmail(TO, subject, body);
    }

    public void sendOfferDeletedEmail(InternshipOffer offer) {
        String subject = "Offre de stage supprimée";
        String body = generateOfferHtml(" Une offre a été supprimée :", offer);
        sendHtmlEmail(TO, subject, body);
    }

    private void sendHtmlEmail(String to, String subject, String bodyContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(FROM);
            helper.setText(bodyContent, true);

            mailSender.send(message);
            System.out.println(" Email envoyé à " + to);
        } catch (MessagingException e) {
            System.err.println(" Erreur d'envoi : " + e.getMessage());
        }
    }

    private String generateOfferHtml(String intro, InternshipOffer offer) {
        return "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2c3e50;'>" + intro + "</h2>"
                + "<p><strong> Titre :</strong> " + safe(offer.getTitle()) + "</p>"
                + "<p><strong> Entreprise :</strong> " + safe(offer.getCompanyName()) + "</p>"
                + "<p><strong>Lieu :</strong> " + safe(offer.getLocation()) + "</p>"
                + "<p><strong> Durée :</strong> " + offer.getDuration() + " semaines</p>"
                + "<p><strong> Type :</strong> " + safe(String.valueOf(offer.getStageType())) + "</p>"
                + "<p><strong> Rémunéré :</strong> " + yesNo(offer.getPaid()) + "</p>"
                + "<p><strong> Télétravail :</strong> " + yesNo(offer.getRemote()) + "</p>"
                + "<p><strong> Description :</strong> " + safe(offer.getJobDescription()) + "</p>"
                + "<p><strong> Date de début :</strong> " + safe(offer.getStartDate()) + "</p>"
                + "</body></html>";
    }


    private String safe(String value) {
        return value != null ? value : "Non précisé";
    }

    private String yesNo(Boolean value) {
        return value != null ? (value ? "Oui" : "Non") : "Non précisé";
    }


}
