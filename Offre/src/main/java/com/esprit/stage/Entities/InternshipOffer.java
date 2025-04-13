package com.esprit.stage.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternshipOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private String location;
    private int duration;

    @Enumerated(EnumType.STRING)
    private StageType stageType;

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean remote;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean paid;

    private String startDate;
    private String jobDescription;
    private LocalDate creationDate;  // Add creationDate

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "internship_offer_image_urls", joinColumns = @JoinColumn(name = "internship_offer_id"))
    @Column
    private List<String> imageUrls;




    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getRemote() {
        return remote;
    }

    public void setRemote(Boolean remote) {
        this.remote = remote;
    }


    @Override
    public String toString() {
        return "InternshipOffer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", duration=" + duration +
                ", stageType=" + stageType +
                ", offerStatus=" + offerStatus +
                ", remote=" + remote +
                ", paid=" + paid +
                ", startDate='" + startDate + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }
}