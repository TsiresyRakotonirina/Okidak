package com.projet.Okidak.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction_event")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime date_trans;

    @NotNull
    @Column
    private Long id_campaign;

    @NotNull
    @Column
    private Long id_campaign_video;

    
    @Column
    private Integer impression;

    @Column
    private Integer lancement;
    
    @Column
    private Integer vue;
    
    @Column
    private Integer skip_video;
    
    @Column
    private Integer quart_lecture;
    
    @Column
    private Integer demi_lecture;
    
    @Column
    private Integer troisquart_lecture;
    
    @Column
    private Integer fin_lecture;

}
