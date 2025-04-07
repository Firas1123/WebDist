package com.esprit.stage.Service;

import com.esprit.stage.Entities.Notification;
import com.esprit.stage.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;


    public void sendNotification(String message, String recipient) {
        Notification notification = Notification.builder()
                .message(message)
                .recipient(recipient)
                .timestamp(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
