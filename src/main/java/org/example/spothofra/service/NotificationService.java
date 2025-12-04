package org.example.spothofra.service;

import org.example.spothofra.entity.Notification;
import org.example.spothofra.enums.CanalNotification;
import org.example.spothofra.enums.TypeNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    Notification creerNotificationPourIncident(Long incidentId,
                                               Long destinataireId,
                                               String message,
                                               TypeNotification type,
                                               CanalNotification canal);

    Page<Notification> getNotificationsUtilisateur(Long utilisateurId, Pageable pageable);

    void marquerCommeLue(Long notificationId);
}
