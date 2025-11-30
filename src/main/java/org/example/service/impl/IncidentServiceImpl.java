package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.*;
import org.example.enums.Priorite;
import org.example.enums.StatutIncident;
import org.example.repository.*;
import org.example.service.IncidentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CategorieIncidentRepository categorieIncidentRepository;
    private final QuartierRepository quartierRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;

    @Override
    public Incident signalerIncident(Incident incident,
                                     Long citoyenId,
                                     Long categorieId,
                                     Long quartierId) {

        Utilisateur citoyen = utilisateurRepository.findById(citoyenId)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable avec id " + citoyenId));

        CategorieIncident categorie = categorieIncidentRepository.findById(categorieId)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable avec id " + categorieId));

        Quartier quartier = quartierRepository.findById(quartierId)
                .orElseThrow(() -> new IllegalArgumentException("Quartier introuvable avec id " + quartierId));

        incident.setCitoyen(citoyen);
        incident.setCategorie(categorie);
        incident.setQuartier(quartier);

        if (incident.getDateSignalement() == null) {
            incident.setDateSignalement(LocalDateTime.now());
        }
        if (incident.getStatutCourant() == null) {
            incident.setStatutCourant(StatutIncident.SIGNALE);
        }
        if (incident.getPriorite() == null) {
            incident.setPriorite(Priorite.NORMALE);
        }

        Incident saved = incidentRepository.save(incident);

        // historique initial
        HistoriqueStatut histo = HistoriqueStatut.builder()
                .incident(saved)
                .acteur(citoyen)
                .ancienStatut(StatutIncident.SIGNALE)
                .nouveauStatut(StatutIncident.SIGNALE)
                .dateChangement(LocalDateTime.now())
                .commentaire("Incident signalé par le citoyen")
                .build();
        historiqueStatutRepository.save(histo);

        return saved;
    }

    @Override
    public Incident assignerAgent(Long incidentId, Long agentId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident introuvable avec id " + incidentId));

        Utilisateur agent = utilisateurRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable avec id " + agentId));

        incident.getAgentsAssignes().add(agent);

        // si incident était "Signalé", on le passe à "Pris en charge"
        StatutIncident ancien = incident.getStatutCourant();
        if (ancien == StatutIncident.SIGNALE) {
            incident.setStatutCourant(StatutIncident.PRIS_EN_CHARGE);
        }

        Incident saved = incidentRepository.save(incident);

        HistoriqueStatut histo = HistoriqueStatut.builder()
                .incident(saved)
                .acteur(agent)
                .ancienStatut(ancien)
                .nouveauStatut(saved.getStatutCourant())
                .dateChangement(LocalDateTime.now())
                .commentaire("Incident assigné à un agent")
                .build();
        historiqueStatutRepository.save(histo);

        return saved;
    }

    @Override
    public Incident changerStatut(Long incidentId,
                                  StatutIncident nouveauStatut,
                                  Long acteurId,
                                  String commentaire) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IllegalArgumentException("Incident introuvable avec id " + incidentId));

        Utilisateur acteur = utilisateurRepository.findById(acteurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + acteurId));

        StatutIncident ancien = incident.getStatutCourant();
        incident.setStatutCourant(nouveauStatut);

        if (nouveauStatut == StatutIncident.RESOLU) {
            incident.setDateResolution(LocalDateTime.now());
        } else if (nouveauStatut == StatutIncident.CLOTURE) {
            incident.setDateCloture(LocalDateTime.now());
        }

        Incident saved = incidentRepository.save(incident);

        HistoriqueStatut histo = HistoriqueStatut.builder()
                .incident(saved)
                .acteur(acteur)
                .ancienStatut(ancien)
                .nouveauStatut(nouveauStatut)
                .dateChangement(LocalDateTime.now())
                .commentaire(commentaire)
                .build();
        historiqueStatutRepository.save(histo);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incident> listerIncidentsCitoyen(Long citoyenId, Pageable pageable) {
        return incidentRepository.findByCitoyen(
                utilisateurRepository.findById(citoyenId)
                        .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable avec id " + citoyenId)),
                pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incident> listerIncidentsAgent(Long agentId, Pageable pageable) {
        return incidentRepository.findByAgentsAssignes_Id(agentId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incident> rechercherParStatut(StatutIncident statut, Pageable pageable) {
        return incidentRepository.findByStatutCourant(statut, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incident> rechercherParPeriode(LocalDateTime debut, LocalDateTime fin, Pageable pageable) {
        return incidentRepository.findByDateSignalementBetween(debut, fin, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Incident> findById(Long id) {
        return incidentRepository.findById(id);
    }
}
