package org.example.spothofra.repository;

import org.example.spothofra.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findByIncident_Id(Long incidentId);
}
