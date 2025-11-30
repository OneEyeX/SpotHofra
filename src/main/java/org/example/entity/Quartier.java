package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quartiers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quartier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 10)
    private String codePostal;

    @Column(length = 100)
    private String ville;

    @OneToMany(mappedBy = "quartier")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Incident> incidents = new ArrayList<>();
}
