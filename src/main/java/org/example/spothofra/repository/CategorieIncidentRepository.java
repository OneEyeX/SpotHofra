package org.example.spothofra.repository;

import org.example.spothofra.entity.CategorieIncident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieIncidentRepository extends JpaRepository<CategorieIncident, Long> {

    Optional<CategorieIncident> findByLibelle(String libelle);

    boolean existsByLibelle(String libelle);
}
