package org.example.spothofra.service;

import org.example.spothofra.entity.Rapport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RapportService {

    Rapport genererRapport(LocalDate periodeDebut, LocalDate periodeFin, Long auteurId);

    List<Rapport> findRapportsByAuteur(Long auteurId);

    Optional<Rapport> findById(Long id);
}
