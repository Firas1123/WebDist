package com.esprit.stage.Repository;

import com.esprit.stage.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long count();
}
