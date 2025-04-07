package com.esprit.stage.Entities;

import lombok.Data;

@Data
public class StatsDTO {
    // Internship Offers
    private long totalOffers;
    private long openOffers;
    private long closedOffers;
    private long archivedOffers;
    private long paidOffers;
    private long remoteOffers;
    private double averageDuration;

    // Applications
    private long totalApplications;
    private double avgApplicationsPerOffer;

    // Notifications
    private long totalNotifications;
}