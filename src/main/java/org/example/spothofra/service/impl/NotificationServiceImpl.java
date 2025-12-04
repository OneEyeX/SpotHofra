package org.example.spothofra.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.spothofra.entity.Incident;
import org.example.spothofra.entity.Notification;
import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.enums.CanalNotification;
import org.example.spothofra.enums.TypeNotification;
import org.example.spothofra.repository.IncidentRepository;
import org.example.spothofra.repository.NotificationRepository;
import org.example.spothofra.repository.UtilisateurRepository;
import org.example.spothofra.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final IncidentRepository incidentRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Notification creerNotificationPourIncident(Long incidentId,
                                                      Long destinataireId,
                                                      String message,
                                                      TypeNotification type,
                                                      CanalNotification canal) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident introuvable avec id " + incidentId));

        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + destinataireId));

        Notification notification = Notification.builder()
                .incident(incident)
                .destinataire(destinataire)
                .message(message)
                .type(type)
                .canal(canal)
                .dateEnvoi(LocalDateTime.now())
                .lu(false)
                .build();

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsUtilisateur(Long utilisateurId, Pageable pageable) {
        Utilisateur destinataire = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + utilisateurId));
        return notificationRepository.findByDestinataire(destinataire, pageable);
    }

    @Override
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification introuvable avec id " + notificationId));
        notification.setLu(true);
        notificationRepository.save(notification);
    }
}
