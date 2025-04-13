package com.example.reclamationweb.Service;

import com.example.reclamationweb.Entities.Reclamation;
import com.example.reclamationweb.Repository.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EmailService emailService;

    private final List<String> badWords = List.of("merde", "nul", "arnaque", "incompétent");

    private String censorBadWords(String text) {
        String censoredText = text;
        for (String badWord : badWords) {
            String regex = "(?i)" + badWord; // insensible à la casse
            censoredText = censoredText.replaceAll(regex, "******");
        }
        return censoredText;
    }

    public Reclamation addReclamation(Reclamation reclamation) {
        // Nettoyage du contenu
        reclamation.setTitle(censorBadWords(reclamation.getTitle()));
        reclamation.setDescription(censorBadWords(reclamation.getDescription()));

        Reclamation saved = reclamationRepository.save(reclamation);

        // Envoi de l’e-mail de notification
        emailService.sendEmail(
                "voltride6@gmail.com",
                "Nouvelle réclamation reçue",
                "Titre : " + saved.getTitle() + "\nDescription : " + saved.getDescription()
        );

        return saved;
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    public Reclamation updateReclamation(Long id, Reclamation updatedReclamation) {
        return reclamationRepository.findById(id).map(reclamation -> {
            reclamation.setTitle(censorBadWords(updatedReclamation.getTitle()));
            reclamation.setDescription(censorBadWords(updatedReclamation.getDescription()));
            reclamation.setStatus(updatedReclamation.getStatus());
            return reclamationRepository.save(reclamation);
        }).orElseThrow(() -> new RuntimeException("Reclamation non trouvée"));
    }

    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
    }
}
