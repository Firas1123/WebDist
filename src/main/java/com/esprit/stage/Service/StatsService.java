package com.esprit.stage.Service;

import com.esprit.stage.Entities.StatsDTO;
import com.esprit.stage.Repository.InternshipOfferRepository;
import com.esprit.stage.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    @Autowired
    private InternshipOfferRepository offerRepository;



    @Autowired
    private NotificationRepository notificationRepository;

    public StatsDTO getAllStats() {
        StatsDTO stats = new StatsDTO();

        // Internship Offers Stats
        stats.setTotalOffers(offerRepository.count());
        stats.setOpenOffers(offerRepository.countOpenOffers());
        stats.setClosedOffers(offerRepository.countClosedOffers());
        stats.setArchivedOffers(offerRepository.countArchivedOffers());
        stats.setPaidOffers(offerRepository.countPaidOffers());
        stats.setRemoteOffers(offerRepository.countRemoteOffers());
        Double avgDuration = offerRepository.findAverageDuration();
        stats.setAverageDuration(avgDuration != null ? avgDuration : 0.0);



        // Notifications Stats
        stats.setTotalNotifications(notificationRepository.count());

        return stats;
    }
}