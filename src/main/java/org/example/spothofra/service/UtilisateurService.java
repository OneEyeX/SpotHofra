package org.example.spothofra.service;

import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.enums.RoleName;

import java.util.List;
import java.util.Optional;

public interface UtilisateurService {

    Utilisateur registerCitizen(Utilisateur utilisateur);

    Utilisateur createUserWithRole(Utilisateur utilisateur, RoleName roleName);

    List<Utilisateur> findAll();

    Optional<Utilisateur> findById(Long id);

    Optional<Utilisateur> findByEmail(String email);

    void deactivateUser(Long id);
}
