package org.example.spothofra.repository;

import org.example.spothofra.entity.Rapport;
import org.example.spothofra.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RapportRepository extends JpaRepository<Rapport, Long> {

    // Rapports créés par un admin
    List<Rapport> findByAuteur(Utilisateur auteur);

    // Rapports générés dans une certaine période
    List<Rapport> findByDateGenerationBetween(LocalDateTime debut, LocalDateTime fin);
}
