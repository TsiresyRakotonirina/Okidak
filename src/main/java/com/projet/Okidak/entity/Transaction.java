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

    @NotNull
    @Column
    private Long id_campaign;

    @NotNull
    @Column
    private Long id_campaign_video;

    @Column
    private LocalDateTime date_trans;
    
    @Column
    private Boolean impression;

    @Column
    private Boolean lancement;
    
    @Column
    private Boolean vue;
    
    @Column
    private Boolean skip_video;
    
    @Column
    private Boolean quart_lecture;
    
    @Column
    private Boolean demi_lecture;
    
    @Column
    private Boolean troisquart_lecture;
    
    @Column
    private Boolean fin_lecture;

}
