package com.esprit.stage.Controller;

import com.esprit.stage.Entities.InternshipOffer;
import com.esprit.stage.Service.InternshipOfferService;
import com.esprit.stage.dto.InternshipOfferResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/internship-offers")
public class InternshipOfferController {

    private final InternshipOfferService internshipOfferService;

    public InternshipOfferController(InternshipOfferService internshipOfferService) {
        this.internshipOfferService = internshipOfferService;
    }

    @PostMapping("/add")
    public InternshipOffer addInternshipOffer(
            @ModelAttribute InternshipOffer offer,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        // Affichage des valeurs reçues pour debug
        System.out.println("Paid: " + offer.getPaid());
        System.out.println("Remote: " + offer.getRemote());

        return internshipOfferService.addInternshipOffer(offer, files);
    }

    @GetMapping("/all")
    public List<InternshipOffer> getAllInternshipOffers() {
        return internshipOfferService.getAllInternshipOffers();
    }

    @GetMapping("/{id}")
    public InternshipOffer getInternshipOfferById(@PathVariable Long id) {
        return internshipOfferService.getInternshipOfferById(id);
    }

    @PutMapping("/update/{id}")
    public InternshipOffer updateInternshipOffer(
            @PathVariable Long id,
            @ModelAttribute InternshipOffer updatedOffer,
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) throws IOException {
        return internshipOfferService.updateInternshipOffer(id, updatedOffer, files);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteInternshipOffer(@PathVariable Long id) throws IOException {
        internshipOfferService.deleteInternshipOffer(id);
        return "Internship Offer deleted successfully!";
    }


    @GetMapping("/search/title")
    public List<InternshipOffer> searchInternshipOffersByTitle(@RequestParam String title) {
        return internshipOfferService.searchByTitle(title);
    }

    @GetMapping("/search/status")
    public List<InternshipOffer> searchInternshipOffersByStatus(@RequestParam String status) {
        return internshipOfferService.searchByStatus(status);
    }
    // ✅ Ajout météo
    @GetMapping("/with-weather")
    public List<InternshipOfferResponseDTO> getAllOffersWithWeather() {
        return internshipOfferService.getAllOffersWithWeather();
    }

}
