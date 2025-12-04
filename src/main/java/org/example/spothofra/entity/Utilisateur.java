package org.example.spothofra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String motDePasseHash;

    @Column(length = 30)
    private String telephone;

    @Column(nullable = false)
    private LocalDateTime dateInscription;

    @Column(nullable = false)
    private boolean actif;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @PrePersist
    public void prePersist() {
        if (dateInscription == null) {
            dateInscription = LocalDateTime.now();
        }
        // actif par défaut à true si non explicitement rempli
        if (!this.actif) {
            this.actif = true;
        }
    }
}
