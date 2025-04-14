package com.example.mscandidature.client;

import com.example.mscandidature.dto.InternshipOfferDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "Stage") // name matches spring.application.name of the offer service
public interface OfferFeignClient {

    @GetMapping("internship-offers/all")
    public List<InternshipOfferDto> getAllInternshipOffers();


    @GetMapping("internship-offers/{id}")
    public InternshipOfferDto getInternshipOfferById(@PathVariable Long id);
}

