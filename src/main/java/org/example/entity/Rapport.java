package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rapports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false)
    private LocalDateTime dateGeneration;

    private LocalDate periodeDebut;
    private LocalDate periodeFin;

    private Long nbIncidents;

    private Double tempsMoyenResolution;

    @Column(length = 500)
    private String cheminFichier;

    // Auteur (admin)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Utilisateur auteur;

    // Incidents inclus dans ce rapport
    @ManyToMany
    @JoinTable(
            name = "rapport_incidents",
            joinColumns = @JoinColumn(name = "rapport_id"),
            inverseJoinColumns = @JoinColumn(name = "incident_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Incident> incidentsInclus = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (dateGeneration == null) {
            dateGeneration = LocalDateTime.now();
        }
    }
}
