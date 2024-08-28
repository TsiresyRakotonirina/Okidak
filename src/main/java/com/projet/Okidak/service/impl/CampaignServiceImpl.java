package com.projet.Okidak.service.impl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projet.Okidak.dto.CampaignDto;
import com.projet.Okidak.entity.Campaign;
import com.projet.Okidak.entity.Campaign_video;
import com.projet.Okidak.repository.CampaignRepository;
import com.projet.Okidak.repository.Campaign_videoRepository;
import com.projet.Okidak.service.CampaignService;

// import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CampaignServiceImpl implements CampaignService{
    
    private CampaignRepository campaignRepository;
    private Campaign_videoRepository campaign_videoRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository, Campaign_videoRepository campaign_videoRepository){
        this.campaignRepository = campaignRepository;
        this.campaign_videoRepository = campaign_videoRepository;
    }

    @Override 
    public Campaign findCampaignByName(String name){
        return campaignRepository.findByName(name);
    }

    @Override
    public List<Campaign> findAllCampaigns(){
        return campaignRepository.findAll();
    }

    // private Campaign_video findCampaign_videoByName(String name){
    //     Campaign_video campaignVideo = campaign_videoRepository.findByName(name);
    //     if (campaignVideo == null) {
    //         throw new EntityNotFoundException("Campaign video not found with name: " + name);
    //     }
    //     return campaignVideo;
    // }

    private Campaign_video findCampaign_videoByName(String name){
        return campaign_videoRepository.findByName(name);
    }

    private void saveCampaign_video(Campaign_video campaign_video){

        Campaign_video existingCampaign_video = findCampaign_videoByName(campaign_video.getName());

        if(existingCampaign_video != null && existingCampaign_video.getName() != null && !existingCampaign_video.getName().isEmpty()){
            throw new IllegalArgumentException("A campaign video with the name '" + campaign_video.getName() + "' already exists."); 
        }

        campaign_videoRepository.save(campaign_video);

    }

    private String pathFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String uploadDir = "C:/Users/User/Documents/Github/Okidak/src/main/resources/uploadDir";  // Chemin où les fichiers seront enregistrés
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    // "Opérations de sauvegarde interdépendantes"
    @Transactional
    private void saveCampaignWithVideo(Campaign campaign, Campaign_video campaign_video) {
        
        saveCampaign_video(campaign_video);
        campaign.setCampaign_video(campaign_video);
        campaignRepository.save(campaign);

    }


    @Override
    public void saveCampaign(CampaignDto campaignDto){

        Campaign campaign = new Campaign();
        Campaign_video campaign_video = new Campaign_video();

        campaign.setName(campaignDto.getName());
        campaign.setStatus(campaignDto.getStatus());
        campaign.setDate_creation(campaignDto.getDate_creation());
        campaign.setDate_modification(campaignDto.getDate_modification());
        campaign.setDate_debut(campaignDto.getDate_debut());
        campaign.setDate_fin(campaignDto.getDate_fin());
        campaign.setBudget(campaignDto.getBudget());
        campaign.setVue_max(campaignDto.getVue_max());
        campaign.setType(campaignDto.getType());
        campaign.setAnnonceur(campaignDto.getAnnonceur());
        
        campaign_video.setName(campaignDto.getName_campaign_video());
        campaign_video.setUrlVideo(campaignDto.getUrlVideo());
        campaign_video.setDescription(campaignDto.getDescription());

        try {
            String logoBeginBytes = pathFile(campaignDto.getLogo_begin());
            String logoEndBytes = pathFile(campaignDto.getLogo_end());
            campaign_video.setLogo_begin(logoBeginBytes);
            campaign_video.setLogo_end(logoEndBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du traitement des fichiers logo", e);
        }

        saveCampaignWithVideo(campaign,campaign_video);

    }


    // byte[] logoBeginBytes = null;
    // byte[] logoEndBytes = null;

    // try (InputStream inputStream = campaignDto.getLogo_begin().getInputStream()) {
        //     logoBeginBytes = inputStream.readAllBytes();
        // } catch (IOException e) {
        //     // Gérez l'exception, par exemple en enregistrant une erreur ou en lançant une exception personnalisée
        //     e.printStackTrace(); // Enregistrer l'exception dans les logs ou gérer autrement
        // }
        
        // try (InputStream inputStream = campaignDto.getLogo_end().getInputStream()) {
        //     logoEndBytes = inputStream.readAllBytes();
        // }catch (IOException e) {
        //     // Gérez l'exception, par exemple en enregistrant une erreur ou en lançant une exception personnalisée
        //     e.printStackTrace(); // Enregistrer l'exception dans les logs ou gérer autrement
        // }

        // if (logoBeginBytes != null) {
        //     campaign_video.setLogo_begin(logoBeginBytes);
        // }
        
        // if (logoEndBytes != null) {
        //     campaign_video.setLogo_end(logoEndBytes);
        // }

         // try {
        //     byte[] logoBeginBytes = campaignDto.getLogo_begin().getBytes();
        //     System.out.println("Valeur de logo begin : " + Arrays.toString(logoBeginBytes));
        // } catch (IOException e) {
        //     System.err.println("Erreur lors de la lecture du fichier logo_begin : " + e.getMessage());
        //     e.printStackTrace();
        // }



}
