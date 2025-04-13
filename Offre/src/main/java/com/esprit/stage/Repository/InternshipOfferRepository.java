package com.esprit.stage.Repository;

import com.esprit.stage.Entities.InternshipOffer;
import com.esprit.stage.Entities.OfferStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, Long> {
    List<InternshipOffer> findByOfferStatus(OfferStatus offerStatus);

    List<InternshipOffer> findByTitleContainingIgnoreCase(String title);

    long count();

    @Query("SELECT COUNT(o) FROM InternshipOffer o WHERE o.offerStatus = 'OPEN'")
    long countOpenOffers();

    @Query("SELECT COUNT(o) FROM InternshipOffer o WHERE o.offerStatus = 'CLOSED'")
    long countClosedOffers();

    @Query("SELECT COUNT(o) FROM InternshipOffer o WHERE o.offerStatus = 'ARCHIVED'")
    long countArchivedOffers();

    @Query("SELECT COUNT(o) FROM InternshipOffer o WHERE o.paid = true")
    long countPaidOffers();

    @Query("SELECT COUNT(o) FROM InternshipOffer o WHERE o.remote = true")
    long countRemoteOffers();

    @Query("SELECT AVG(o.duration) FROM InternshipOffer o")
    Double findAverageDuration();
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM internship_offer_image_urls WHERE internship_offer_id = :offerId", nativeQuery = true)
    void deleteImagesByOfferId(@Param("offerId") Long offerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM InternshipOffer i WHERE i.id = :id")
    void deleteInternshipOfferById(@Param("id") Long id);
}



