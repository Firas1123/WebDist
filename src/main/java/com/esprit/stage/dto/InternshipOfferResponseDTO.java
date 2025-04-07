package com.esprit.stage.dto;

import com.esprit.stage.Entities.OfferStatus;
import com.esprit.stage.Entities.StageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InternshipOfferResponseDTO {
    private Long id;
    private String title;
    private String companyName;
    private String location;
    private int duration;
    private StageType stageType;
    private OfferStatus offerStatus;
    private Boolean remote;
    private Boolean paid;
    private String startDate;
    private String jobDescription;
    private LocalDate creationDate;
    private List<String> imageUrls;

    private WeatherResponse weather;
}
