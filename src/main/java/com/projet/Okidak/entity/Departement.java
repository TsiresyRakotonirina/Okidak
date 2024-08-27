package com.projet.Okidak.entity;

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
@Table(name="departements")
public class Departement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_departement;

    @Column(nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy = "departement")
    private List<Annonceur> annonceurs;

}
