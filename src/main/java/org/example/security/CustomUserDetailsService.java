package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.entity.Utilisateur;
import org.example.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = email envoyé par le formulaire (champ "username")
        Utilisateur utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur introuvable avec email : " + username)
                );

        return new CustomUserDetails(utilisateur);
    }
}
