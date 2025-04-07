package com.example.mscandidature.Controller;

import com.example.mscandidature.Entity.Candidature;
import com.example.mscandidature.Repository.CandidatureRepository;
import com.example.mscandidature.Service.CandidatureService;
import com.example.mscandidature.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Candidature")
public class CandidatureRestAPI {
    private String title="hello";
    @RequestMapping("/hello")
    public String sayHello(){
        System.out.println(title);
        return title;
    }
    @Autowired
    private CandidatureService candidatureService;
    @Autowired
    private CandidatureRepository candidatureRepository;

    // Get all candidatures
    @RequestMapping
    public ResponseEntity<List<Candidature>> getAllCandidatures() {
        return new ResponseEntity<>(candidatureService.getAllCandidatures(), HttpStatus.OK);
    }

    // Add a new candidature
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Candidature> addCandidature(@RequestBody Candidature candidature) {
        // Ajouter la candidature dans la base de données
        Candidature savedCandidature = candidatureService.addCandidature(candidature);

        // Après avoir ajouté la candidature, envoyer un e-mail
        String subject = "Nouvelle Candidature";

        // Corps de l'email en HTML
        String body = "<html>" +
                "<body>" +
                "<h2 style='color:#4CAF50;'>Nouvelle Candidature</h2>" +
                "<p style='font-size: 16px;'>Une nouvelle candidature a été ajoutée. Voici les détails :</p>" +
                "<table style='border-collapse: collapse; width: 100%;'>" +
                "<tr>" +
                "<th style='border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;'>Nom</th>" +
                "<td style='border: 1px solid #ddd; padding: 8px;'>" + savedCandidature.getFullName() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th style='border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;'>Email</th>" +
                "<td style='border: 1px solid #ddd; padding: 8px;'>" + savedCandidature.getEmail() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th style='border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;'>Ville</th>" +
                "<td style='border: 1px solid #ddd; padding: 8px;'>" + savedCandidature.getCity() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<th style='border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;'>Téléphone</th>" +
                "<td style='border: 1px solid #ddd; padding: 8px;'>" + savedCandidature.getPhone() + "</td>" +
                "</tr>" +
                "</table>" +
                "<p style='font-size: 16px;'>Merci de prendre en charge cette candidature.</p>" +
                "</body>" +
                "</html>";

        // Remplacer l'adresse ci-dessous par celle du destinataire
        try {
            emailService.sendEmail("maarefcharfeddine3@gmail.com", subject, body);
        } catch (Exception e) {
            // Gérer l'exception, par exemple en loggant l'erreur
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retourner un statut d'erreur si nécessaire
        }

        // Retourner la réponse avec la candidature ajoutée
        return new ResponseEntity<>(savedCandidature, HttpStatus.CREATED);
    }


    // Get a candidature by ID
    @RequestMapping("{id}")
    public ResponseEntity<Candidature> getCandidatureById(@PathVariable int id) {
        Candidature candidature = candidatureService.getCandidatureById(id);
        if (candidature != null) {
            return new ResponseEntity<>(candidature, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing candidature
    @PutMapping("{id}")
    public ResponseEntity<Candidature> updateCandidature(@PathVariable int id, @RequestBody Candidature newCandidature) {
        Candidature updatedCandidature = candidatureService.updateCandidature(id, newCandidature);
        if (updatedCandidature != null) {
            return new ResponseEntity<>(updatedCandidature, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a candidature
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCandidature(@PathVariable int id) {
        String result = candidatureService.deleteCandidature(id);
        if ("Candidature supprimée".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/search")
    public List<Candidature> searchCandidatures(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String city) {
        return candidatureRepository.searchCandidatures(fullName, email, city);
}
}
