package org.example.repository;

import org.example.entity.HistoriqueStatut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

    List<HistoriqueStatut> findByIncident_IdOrderByDateChangementAsc(Long incidentId);
}
