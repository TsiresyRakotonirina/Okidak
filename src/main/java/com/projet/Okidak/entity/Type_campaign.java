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
@Table(name="type_campaign")
public class Type_campaign {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_type_campaign;

    @Column(nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Campaign> campaigns; 

}
