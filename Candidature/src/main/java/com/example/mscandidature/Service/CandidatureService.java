package com.example.mscandidature.Service;

import com.example.mscandidature.Entity.Candidature;
import com.example.mscandidature.Repository.CandidatureRepository;
import com.example.mscandidature.client.OfferFeignClient;
import com.example.mscandidature.dto.InternshipOfferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidatureService {
    @Autowired
    private CandidatureRepository candidatureRepository;
    @Autowired
    private OfferFeignClient offerFeignClient;


    public InternshipOfferDto fetchOfferDetails(long offerId) {
        return offerFeignClient.getInternshipOfferById(offerId);
    }
    public List<InternshipOfferDto> getOffers(){
        return offerFeignClient.getAllInternshipOffers();
    }
    public List<InternshipOfferDto> getFavoriteOffers(int candidateId) {
        Candidature candidate = candidatureRepository.findById(candidateId).get();
        return candidate.getFavoriteOffers().stream()
                .map(offerFeignClient::getInternshipOfferById)
                .collect(Collectors.toList());
    }
    public void saveFavoriteoffres(int candidateId, long offreId) {
        Candidature candidate = candidatureRepository.findById(candidateId).get();
        candidate.getFavoriteOffers().add(offreId);
        candidatureRepository.save(candidate);
    }

    // Add a new Candidature
    public Candidature addCandidature(Candidature candidature) {
        candidature.setSubmissionDate(LocalDateTime.now());  // Set the current date and time
        return candidatureRepository.save(candidature);
    }

    // Get all Candidatures
    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    // Update an existing Candidature
    public Candidature updateCandidature(int id, Candidature newCandidature) {
        Optional<Candidature> optionalCandidature = candidatureRepository.findById(id);
        if (optionalCandidature.isPresent()) {
            Candidature existing = optionalCandidature.get();
            existing.setUserId(newCandidature.getUserId());
            existing.setOfferId(newCandidature.getOfferId());
            existing.setFullName(newCandidature.getFullName());
            existing.setEmail(newCandidature.getEmail());
            existing.setPhone(newCandidature.getPhone());
            existing.setCity(newCandidature.getCity());
            return candidatureRepository.save(existing);
        } else {
            return null; // Handle the case where the candidature is not found
        }
    }

    // Delete a Candidature by ID
    public String deleteCandidature(int id) {
        if (candidatureRepository.existsById(id)) {
            candidatureRepository.deleteById(id);
            return "Candidature supprimée";
        } else {
            return "Candidature non trouvée";
        }
    }

    // Get a Candidature by ID
    public Candidature getCandidatureById(int id) {
        return candidatureRepository.findById(id).orElse(null);
    }
}
