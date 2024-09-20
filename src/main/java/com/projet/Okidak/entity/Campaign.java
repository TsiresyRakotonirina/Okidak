package com.projet.Okidak.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name="campaigns")
public class Campaign {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date_creation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date_modification;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date_debut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date_fin;

    @Column(nullable=false)
    private BigDecimal budget;

    @Column(nullable=false)
    private Long vue_max;

    @ManyToOne @JoinColumn(name = "id_type_campaign", nullable = false)
    private Type_campaign type;
    
    
    @ManyToOne @JoinColumn(name = "id_annonceur", nullable = false)
    private Annonceur annonceur;




    @OneToOne  
    @JoinColumn( name="id_campaign_video", nullable=false )
    private Campaign_video campaign_video;

    @OneToMany(mappedBy = "campaign") 
    private List<Campaign_periode> campaign_periode = new ArrayList<>();

    @OneToMany(mappedBy = "campaign")
    @JsonManagedReference
    private List<Campaign_carousel> campaign_carousel = new ArrayList<>();




    @Transient
    private String carouselJson; // Champ temporaire pour stocker les données JSON
    
}
