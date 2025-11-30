package org.example.repository;

import org.example.entity.Notification;
import org.example.entity.Utilisateur;
import org.example.enums.TypeNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notifications reçues par un utilisateur
    Page<Notification> findByDestinataire(Utilisateur destinataire, Pageable pageable);

    // Notifications non lues pour un utilisateur
    Page<Notification> findByDestinataireAndLuFalse(Utilisateur destinataire, Pageable pageable);

    // Par type
    Page<Notification> findByDestinataireAndType(Utilisateur destinataire, TypeNotification type, Pageable pageable);
}
