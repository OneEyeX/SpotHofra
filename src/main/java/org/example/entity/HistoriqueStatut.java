package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.StatutIncident;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_statuts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutIncident ancienStatut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutIncident nouveauStatut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    @Column(length = 1000)
    private String commentaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acteur_id", nullable = false)
    private Utilisateur acteur;

    @PrePersist
    public void prePersist() {
        if (dateChangement == null) {
            dateChangement = LocalDateTime.now();
        }
    }
}
