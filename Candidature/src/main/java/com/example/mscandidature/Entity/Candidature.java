package com.example.mscandidature.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor  // No-argument constructor for JPA
@Entity
@Getter
@Setter
public class Candidature implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer offerId;

    private String fullName;
    private String email;
    private String phone;
    private String city;

    private LocalDateTime submissionDate;
    @ElementCollection
    private Set<Long> favoriteOffers = new HashSet<>();

}
