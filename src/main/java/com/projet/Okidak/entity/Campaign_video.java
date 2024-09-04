package com.projet.Okidak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campaign_video")
public class Campaign_video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_campaign_video;

    @Column(nullable=false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String urlVideo;

    @Column(name = "logo_begin")
    private String logo_begin;

    @Column(name = "logo_end")
    private String logo_end;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne (mappedBy = "campaign_video")
    private Campaign campaign;


}
