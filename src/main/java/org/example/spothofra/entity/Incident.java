package org.example.spothofra.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.spothofra.enums.Priorite;
import org.example.spothofra.enums.StatutIncident;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutIncident statutCourant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priorite priorite;

    @Column(length = 255)
    private String adresse;

    private Double latitude;
    private Double longitude;

    private LocalDateTime dateResolution;
    private LocalDateTime dateCloture;

    // ----- Relations -----

    // Catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategorieIncident categorie;

    // Quartier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quartier_id", nullable = false)
    private Quartier quartier;

    // Citoyen qui a signalé l'incident
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citoyen_id", nullable = false)
    private Utilisateur citoyen;

    // Agents municipaux assignés
    @ManyToMany
    @JoinTable(
            name = "incident_agents",
            joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Utilisateur> agentsAssignes = new HashSet<>();

    // Photos associées
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Photo> photos = new ArrayList<>();

    // Historique des statuts
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<HistoriqueStatut> historique = new ArrayList<>();

    // Notifications liées à cet incident
    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Notification> notifications = new ArrayList<>();

    // Rapports dans lesquels cet incident est inclus
    @ManyToMany(mappedBy = "incidentsInclus")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Rapport> rapports = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (dateSignalement == null) {
            dateSignalement = LocalDateTime.now();
        }
        if (statutCourant == null) {
            statutCourant = StatutIncident.SIGNALE;
        }
        if (priorite == null) {
            priorite = Priorite.NORMALE;
        }
    }
}
