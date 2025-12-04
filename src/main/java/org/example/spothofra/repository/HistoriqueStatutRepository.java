package org.example.spothofra.repository;

import org.example.spothofra.entity.HistoriqueStatut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

    List<HistoriqueStatut> findByIncident_IdOrderByDateChangementAsc(Long incidentId);
}
