package com.esprit.stage.Controller;

import com.esprit.stage.Entities.StatsDTO;
import com.esprit.stage.Service.StatisticsService;
import com.esprit.stage.Service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private StatisticsService statisticsService;

    // 📊 API Admin - Nombre d’offres par entreprise
    @GetMapping("/global/offers-by-company")
    public ResponseEntity<Map<String, Long>> getOffersByCompany() {
        return ResponseEntity.ok(statisticsService.getOffersByCompany());

    }


    // 📊 API Admin - Répartition géographique des offres
    @GetMapping("/global/offers-by-location")
    public ResponseEntity<Map<String, Long>> getOffersByLocation() {
        return ResponseEntity.ok(statisticsService.getOffersByLocation());
    }


    // 📈 API Admin - Nombre d’offres créées par jour
    @GetMapping("/global/offers-by-day")
    public ResponseEntity<Map<String, Long>> getOffersByDay() {
        return ResponseEntity.ok(statisticsService.getOffersByDay());
    }





    // 📊 API RH - Nombre d’offres publiées par leur entreprise
    @GetMapping("/company/{companyName}/offers")
    public ResponseEntity<Long> getOffersByCompanyRH(@PathVariable String companyName) {
        return ResponseEntity.ok(statisticsService.getOffersByCompanyRH(companyName));
    }

    // 📈 API RH - Nombre d’offres créées par jour pour leur entreprise
    @GetMapping("/company/{companyName}/offers-by-day")
    public ResponseEntity<Map<String, Long>> getOffersByCompanyPerDay(@PathVariable String companyName) {
        return ResponseEntity.ok(statisticsService.getOffersByCompanyPerDay(companyName));
    }




    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> getStats() {
        StatsDTO stats = statsService.getAllStats();
        return ResponseEntity.ok(stats);
    }
}
