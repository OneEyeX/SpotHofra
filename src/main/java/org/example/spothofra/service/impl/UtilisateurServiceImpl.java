package org.example.spothofra.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.spothofra.entity.Role;
import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.enums.RoleName;
import org.example.spothofra.repository.RoleRepository;
import org.example.spothofra.repository.UtilisateurRepository;
import org.example.spothofra.service.UtilisateurService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private Role getOrCreateRole(RoleName roleName) {
        return roleRepository.findByRole(roleName)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().role(roleName).build()
                ));
    }

    @Override
    public Utilisateur registerCitizen(Utilisateur utilisateur) {
        return createUserWithRole(utilisateur, RoleName.CITOYEN);
    }

    @Override
    public Utilisateur createUserWithRole(Utilisateur utilisateur, RoleName roleName) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé : " + utilisateur.getEmail());
        }

        utilisateur.setMotDePasseHash(passwordEncoder.encode(utilisateur.getMotDePasseHash()));
        utilisateur.setRole(getOrCreateRole(roleName));
        utilisateur.setActif(true);
        if (utilisateur.getDateInscription() == null) {
            utilisateur.setDateInscription(LocalDateTime.now());
        }

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public void deactivateUser(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable avec id " + id));
        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
    }
}
