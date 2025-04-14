package com.esprit.stage.Controller;

import com.esprit.stage.Entities.InternshipOffer;
import com.esprit.stage.Service.InternshipOfferService;
import com.esprit.stage.dto.InternshipOfferResponseDTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Locale;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @GetMapping("/reverse-geocode")
    public ResponseEntity<String> getGovernorateFromCoordinates(
            @RequestParam String lat,
            @RequestParam String lon) {

        try {
            // Remplacer virgule par point ✅
            lat = lat.replace(",", ".");
            lon = lon.replace(",", ".");

            // Affichage debug
            System.out.println("📍 lat = " + lat + ", lon = " + lon);

            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

         // assure-toi d'importer Locale

            String url = String.format(Locale.US,
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%.6f&lon=%.6f&zoom=10&addressdetails=1&accept-language=fr",
                    latitude, longitude);



            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Java Spring Boot")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode addressNode = root.path("address");

            System.out.println("🔎 JSON complet : " + root.toPrettyString());

            String[] fallbackKeys = {"state", "city", "town", "county", "region", "suburb"};
            String governorate = null;

            for (String key : fallbackKeys) {
                JsonNode value = addressNode.path(key);
                if (!value.isMissingNode() && !value.asText().isEmpty()) {
                    governorate = value.asText();
                    break;
                }
            }

            if (governorate == null || governorate.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Governorate not found.");
            }

            return ResponseEntity.ok(governorate);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid latitude or longitude format.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while reverse geocoding: " + e.getMessage());
        }
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

}