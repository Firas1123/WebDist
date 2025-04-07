package com.example.mscandidature.Repository;

import com.example.mscandidature.Entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
    @Query("SELECT c FROM Candidature c WHERE " +
            "(:fullName IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%')))")
    List<Candidature> searchCandidatures(@Param("fullName") String fullName,
                                         @Param("email") String email,
                                         @Param("city") String city);
}
