package com.projet.Okidak.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "campaign_periode")
public class Campaign_periode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_campaign_periode;

    @Column(nullable = false)
    private Long ordre;

    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start_date;

    @Column(nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end_date;

    @Column
    private BigDecimal budget_periode;
    
    @Column
    private Long vue_objectif;


    @ManyToOne @JoinColumn(name = "id_campaign", nullable = false)
    private Campaign campaign;

}
