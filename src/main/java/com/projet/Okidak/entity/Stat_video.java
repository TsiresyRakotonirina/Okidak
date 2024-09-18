package com.projet.Okidak.entity;


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
@Table(name = "statistique_video")
public class Stat_video{
    
    @Id
    @Column(name = "id_stat_unique")
    private String idStatUnique;

    @Column(name = "id_campaign")
    private Long idCampaign;

    @Column(name = "id_campaign_video")
    private Long idCampaignVideo;

    @Column(name = "nb_impression")
    private Long nbImpression;

    @Column(name = "nb_lancement")
    private Long nbLancement;
    
    @Column(name = "nb_vue")
    private Long nbVue;

    @Column(name = "nb_skip_video")
    private Long nbSkipVideo;

    @Column(name = "nb_quart_lecture")
    private Long nbQuartLecture;

    @Column(name = "nb_demi_lecture")
    private Long nbDemiLecture;

    @Column(name = "nb_troisquart_lecture")
    private Long nbTroisquartLecture;

    @Column(name = "nb_fin_lecture")
    private Long nbFinLecture;

}
