package com.esprit.stage.mapper;

import com.esprit.stage.Service.WeatherService;
import com.esprit.stage.dto.InternshipOfferResponseDTO;
import com.esprit.stage.dto.WeatherResponse;
import com.esprit.stage.Entities.InternshipOffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InternshipOfferMapper {

    @Autowired
    private WeatherService weatherService;

    /**
     * Convertit une entité InternshipOffer en DTO enrichi avec les infos météo.
     */
    public InternshipOfferResponseDTO toDTO(InternshipOffer offer) {
        InternshipOfferResponseDTO dto = new InternshipOfferResponseDTO();

        dto.setId(offer.getId());
        dto.setTitle(offer.getTitle());
        dto.setCompanyName(offer.getCompanyName());
        dto.setLocation(offer.getLocation());
        dto.setDuration(offer.getDuration());
        dto.setStageType(offer.getStageType());
        dto.setOfferStatus(offer.getOfferStatus());
        dto.setRemote(offer.getRemote());
        dto.setPaid(offer.getPaid());
        dto.setStartDate(offer.getStartDate());
        dto.setJobDescription(offer.getJobDescription());
        dto.setCreationDate(offer.getCreationDate());
        dto.setImageUrls(offer.getImageUrls());

        // Intégration météo
        WeatherResponse weather = weatherService.getWeatherByCity(offer.getLocation());
        dto.setWeather(weather);

        return dto;
    }
}
