package com.esprit.stage.Service;

import com.esprit.stage.Repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    // 📊 Nombre d’offres par entreprise (global)
    public Map<String, Long> getOffersByCompany() {
        return convertToMap(statisticsRepository.countOffersByCompany());
    }

    // 📈 Nombre d’offres créées par jour (global)
    public Map<String, Long> getOffersByDay() {
        return convertToMap(statisticsRepository.countOffersByDay());
    }
    public Double getSubmissionToOfferRatio() {
        Double ratio = statisticsRepository.getSubmissionToOfferRatio();
        return ratio != null ? ratio : 0.0;  // ✅ Assurer une valeur par défaut
    }



    public Map<String, Long> getOffersByLocation() {
        List<Object[]> results = statisticsRepository.countOffersByLocation();
        return results.stream().collect(Collectors.toMap(
                data -> String.valueOf(data[0]),  // Nom de la ville
                data -> (Long) data[1]           // Nombre d'offres
        ));
    }



    // 📊 Nombre d’offres publiées pour une entreprise spécifique (RH)
    public Long getOffersByCompanyRH(String companyName) {
        return statisticsRepository.countOffersByCompanyRH(companyName);
    }

    // 📈 Nombre d’offres créées par jour pour une entreprise spécifique (RH)
    public Map<String, Long> getOffersByCompanyPerDay(String companyName) {
        return convertToMap(statisticsRepository.countOffersByCompanyPerDay(companyName));
    }




    // 📌 Conversion des résultats en JSON
    private Map<String, Long> convertToMap(List<Object[]> results) {
        return results.stream().collect(Collectors.toMap(
                data -> String.valueOf(data[0]),
                data -> (Long) data[1]
        ));
    }
}
