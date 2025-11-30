package org.example.service;

import org.example.entity.Incident;
import org.example.enums.StatutIncident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IncidentService {

    Incident signalerIncident(Incident incident, Long citoyenId, Long categorieId, Long quartierId);

    Incident assignerAgent(Long incidentId, Long agentId);

    Incident changerStatut(Long incidentId, StatutIncident nouveauStatut, Long acteurId, String commentaire);

    Page<Incident> listerIncidentsCitoyen(Long citoyenId, Pageable pageable);

    Page<Incident> listerIncidentsAgent(Long agentId, Pageable pageable);

    Page<Incident> rechercherParStatut(StatutIncident statut, Pageable pageable);

    Page<Incident> rechercherParPeriode(LocalDateTime debut, LocalDateTime fin, Pageable pageable);

    Optional<Incident> findById(Long id);
}
