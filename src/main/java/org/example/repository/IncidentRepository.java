package org.example.repository;

import org.example.entity.Incident;
import org.example.entity.Utilisateur;
import org.example.enums.Priorite;
import org.example.enums.StatutIncident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    // Incidents créés par un citoyen
    Page<Incident> findByCitoyen(Utilisateur citoyen, Pageable pageable);

    // Incidents assignés à un agent (via la relation ManyToMany)
    Page<Incident> findByAgentsAssignes_Id(Long agentId, Pageable pageable);

    // Recherche par statut
    Page<Incident> findByStatutCourant(StatutIncident statut, Pageable pageable);

    // Recherche par statut + quartier + catégorie (pour filtres avancés)
    Page<Incident> findByStatutCourantAndQuartier_IdAndCategorie_Id(
            StatutIncident statut,
            Long quartierId,
            Long categorieId,
            Pageable pageable
    );

    // Recherche par date de signalement
    Page<Incident> findByDateSignalementBetween(
            LocalDateTime dateDebut,
            LocalDateTime dateFin,
            Pageable pageable
    );

    // Exemple : incidents par priorité
    Page<Incident> findByPriorite(Priorite priorite, Pageable pageable);
}
