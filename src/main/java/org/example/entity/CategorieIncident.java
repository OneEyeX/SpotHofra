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
@Table(name = "categories_incident")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorieIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "categorie")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Incident> incidents = new ArrayList<>();
}
