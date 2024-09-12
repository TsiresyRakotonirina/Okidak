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
        MultipartFile[] img_carousel = campaignDto.getImg_carousel();

        // Vérifie que soit l'un soit l'autre est renseigné, mais pas les deux ou aucun des deux
        boolean isUrlVideoPresent = (urlVideo != null && !urlVideo.isEmpty());
        boolean isVideoLocalPresent = (videoLocal != null && !videoLocal.isEmpty());
        boolean isImg_carouselPresent = (img_carousel != null && img_carousel.length > 0);

        // return isUrlVideoPresent ^ isVideoLocalPresent; // Retourne true si un seul des deux est rempli


         // Compte le nombre d'attributs présents
         int count = 0;
         if (isUrlVideoPresent) count++;
         if (isVideoLocalPresent) count++;
         if (isImg_carouselPresent) count++;
 
         // Retourne true si exactement un attribut est renseigné
         return count == 1;

    }
}
