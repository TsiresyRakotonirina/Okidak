package com.projet.Okidak.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.projet.Okidak.annot.EitherUrlOrFile;
import com.projet.Okidak.entity.Annonceur;
import com.projet.Okidak.entity.Type_campaign;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EitherUrlOrFile(message = "soit file soit url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDto {
    
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private int status;

    @NotEmpty
    private LocalDateTime date_creation;

    @NotEmpty
    private LocalDateTime date_modification;

    @NotEmpty
    private LocalDateTime date_debut;

    @NotEmpty
    private LocalDateTime date_fin;

    @NotEmpty
    @Positive(message = "La valeur doit être positive")
    private BigDecimal budget;

    @NotEmpty
    @Positive(message = "La valeur doit être positive")
    private Long vue_max;

    @NotEmpty
    private Type_campaign type;

    @NotEmpty
    private Annonceur annonceur;

    @NotEmpty
    private String name_campaign_video;

    @NotEmpty
    private String urlVideo;

    @NotEmpty
    private MultipartFile videoLocal;

    @NotEmpty
    private MultipartFile logo_begin;

    @NotEmpty
    private MultipartFile logo_end;


    @NotEmpty
    private String description;

}
