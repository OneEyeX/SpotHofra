package org.example.spothofra.repository;

import org.example.spothofra.entity.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuartierRepository extends JpaRepository<Quartier, Long> {

    List<Quartier> findByVille(String ville);

    List<Quartier> findByCodePostal(String codePostal);
}
