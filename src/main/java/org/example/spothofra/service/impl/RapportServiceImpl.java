package org.example.spothofra.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.spothofra.entity.Incident;
import org.example.spothofra.entity.Rapport;
import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.repository.IncidentRepository;
import org.example.spothofra.repository.RapportRepository;
import org.example.spothofra.repository.UtilisateurRepository;
import org.example.spothofra.service.RapportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RapportServiceImpl implements RapportService {

    private final RapportRepository rapportRepository;
    private final IncidentRepository incidentRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Rapport genererRapport(LocalDate periodeDebut, LocalDate periodeFin, Long auteurId) {

        Utilisateur auteur = utilisateurRepository.findById(auteurId)
                .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable avec id " + auteurId));

        LocalDateTime debutDateTime = periodeDebut.atStartOfDay();
        LocalDateTime finDateTime = periodeFin.atTime(LocalTime.MAX);

        List<Incident> incidents = incidentRepository
                .findByDateSignalementBetween(debutDateTime, finDateTime, null)
                .getContent(); // attention: on pourrait plutôt utiliser un Pageable pour les gros volumes

        long nbIncidents = incidents.size();

        // calcul temps moyen de résolution (en heures)
        double tempsMoyenResolution = incidents.stream()
                .filter(i -> i.getDateResolution() != null)
                .mapToLong(i -> Duration.between(i.getDateSignalement(), i.getDateResolution()).toHours())
                .average()
                .orElse(0.0);

        Rapport rapport = Rapport.builder()
                .titre("Rapport incidents du " + periodeDebut + " au " + periodeFin)
                .periodeDebut(periodeDebut)
                .periodeFin(periodeFin)
                .auteur(auteur)
                .nbIncidents(nbIncidents)
                .tempsMoyenResolution(tempsMoyenResolution)
                .build();

        // TODO: génération réelle du fichier CSV/PDF et affectation de cheminFichier
        // rapport.setCheminFichier("path/to/file.pdf");

        return rapportRepository.save(rapport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rapport> findRapportsByAuteur(Long auteurId) {
        Utilisateur auteur = utilisateurRepository.findById(auteurId)
                .orElseThrow(() -> new IllegalArgumentException("Auteur introuvable avec id " + auteurId));
        return rapportRepository.findByAuteur(auteur);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rapport> findById(Long id) {
        return rapportRepository.findById(id);
    }
}
