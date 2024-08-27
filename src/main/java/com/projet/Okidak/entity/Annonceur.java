package com.projet.Okidak.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="annonceurs")
public class Annonceur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String adresse;

    @ManyToOne @JoinColumn(name = "id_departement", nullable = false)
    private Departement departement;

    @OneToMany(mappedBy = "annonceur")
    List<Campaign> campaigns = new ArrayList<>();


}
