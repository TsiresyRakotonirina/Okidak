package com.projet.Okidak.config;

import com.projet.Okidak.annot.EitherUrlOrFile;
import com.projet.Okidak.dto.CampaignDto;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrlOrFileValidator implements ConstraintValidator<EitherUrlOrFile, CampaignDto> {

    @Override
    public void initialize(EitherUrlOrFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(CampaignDto campaignDto, ConstraintValidatorContext context) {
        String urlVideo = campaignDto.getUrlVideo();
        MultipartFile videoLocal = campaignDto.getVideoLocal();

        // Vérifie que soit l'un soit l'autre est renseigné, mais pas les deux ou aucun des deux
        boolean isUrlVideoPresent = (urlVideo != null && !urlVideo.isEmpty());
        boolean isVideoLocalPresent = (videoLocal != null && !videoLocal.isEmpty());

        return isUrlVideoPresent ^ isVideoLocalPresent; // Retourne true si un seul des deux est rempli
    }
}
