package com.projet.Okidak.service.impl;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projet.Okidak.dto.CampaignDto;
import com.projet.Okidak.entity.Campaign;
import com.projet.Okidak.entity.Campaign_periode;
import com.projet.Okidak.entity.Campaign_video;
import com.projet.Okidak.modele.Interval;
import com.projet.Okidak.repository.CampaignRepository;
import com.projet.Okidak.repository.Campaign_periodeRepository;
import com.projet.Okidak.repository.Campaign_videoRepository;
import com.projet.Okidak.service.CampaignService;

import java.security.SecureRandom;

// import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CampaignServiceImpl implements CampaignService{
    
    private CampaignRepository campaignRepository;
    private Campaign_videoRepository campaign_videoRepository;
    private Campaign_periodeRepository campaign_periodeRepository; 

    public CampaignServiceImpl(CampaignRepository campaignRepository, Campaign_videoRepository campaign_videoRepository, Campaign_periodeRepository campaign_periodeRepository){
        this.campaignRepository = campaignRepository;
        this.campaign_videoRepository = campaign_videoRepository;
        this.campaign_periodeRepository = campaign_periodeRepository;
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

    private void saveCampaign_video(Campaign_video campaign_video) throws Exception {

        Campaign_video existingCampaign_video = findCampaign_videoByName(campaign_video.getName());

        try {

            if(existingCampaign_video != null && existingCampaign_video.getName() != null && !existingCampaign_video.getName().isEmpty()){
                throw new IllegalArgumentException("A campaign video with the name '" + campaign_video.getName() + "' already exists."); 
            }
            campaign_videoRepository.save(campaign_video);

        } catch (Exception e) {
            throw new Exception("Erreur lors de la sauvegarde de campaign Video!",e); 
        }

    }

    private String pathFile(MultipartFile file,String nameCampaign) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Le fichier doit avoir un nom valide.");
        }

        String fileExtension = "";

        // Extraire l'extension du fichier
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = fileName.substring(i);
        }

         // Générer un nombre aléatoire de 12 chiffres
        SecureRandom random = new SecureRandom();
        long randomNumber = Math.abs(random.nextLong() % 1000000000000L); // 12 chiffres

        // gestion nom camapign
        String nom_campaign = nameCampaign.replaceAll(" ", "_");
        String newFileName = randomNumber + "_" + nom_campaign + fileExtension;

        String uploadDir =  "src/main/resources/static/uploadDir";  // Chemin où les fichiers seront enregistrés
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(newFileName);

        String lien_file_base = "/uploadDir/" + newFileName;

        Files.copy(file.getInputStream(), filePath);

        return lien_file_base;

    }

    // "Opérations de sauvegarde interdépendantes"
    @Transactional
    private void saveCampaignWithVideo(Campaign campaign, Campaign_video campaign_video) throws Exception{
        
        saveCampaign_video(campaign_video);
        campaign.setCampaign_video(campaign_video);
        campaignRepository.save(campaign);
        saveCampaign_periode(campaign);

    }


    @Override
    public void saveCampaign(CampaignDto campaignDto) throws Exception{

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
            String logoBeginBytes = pathFile(campaignDto.getLogo_begin(),campaign.getName());
            String logoEndBytes = pathFile(campaignDto.getLogo_end(),campaign.getName());
            campaign_video.setLogo_begin(logoBeginBytes);
            campaign_video.setLogo_end(logoEndBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du traitement des fichiers logo", e);
        }

        saveCampaignWithVideo(campaign,campaign_video);

    }

    // TRAITEMENT CAMPAIGN PERIODE BUDGET/JOUR VUE 

    // calcule periode
    private int calcule_periode(Campaign campaign){
        if (campaign.getDate_fin().isBefore(campaign.getDate_debut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        Duration duration = Duration.between(campaign.getDate_debut(), campaign.getDate_fin());
        long totalHours = duration.toHours();
        double periods = totalHours / 24; 
        return (int) Math.ceil(periods);
    } 

    // calcule budget par periode
    private BigDecimal traitement_budget(Campaign campaign){
        BigDecimal budget = campaign.getBudget();
        BigDecimal nombre_periode = BigDecimal.valueOf(calcule_periode(campaign));
        return budget.divide(nombre_periode, 2, RoundingMode.HALF_UP);
    }

    // // calcule view par periode
    private Long objectif_vue_periode(Campaign campaign){
        Long vue_objectif = campaign.getVue_max();
        Long nombre_periode = Long.valueOf(calcule_periode(campaign));
        return vue_objectif / nombre_periode;
    }

    
    // les intervalles de date (periode) entre 2 dates
    private List<Interval> getDatesPeriod(Campaign campaign){

        List<Interval> intervals = new ArrayList<>();

        if (campaign.getDate_fin().isBefore(campaign.getDate_debut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        
        LocalDateTime fin = campaign.getDate_fin();
        LocalDateTime date_debut_periode = campaign.getDate_debut();

        while (date_debut_periode.isBefore(fin)) {

            LocalDateTime date_fin_periode = date_debut_periode.plusDays(1).minusSeconds(1);

             // La dernière période peut finir à endDateTime si elle est après currentEnd
            if (date_fin_periode.isAfter(fin)) {
                date_fin_periode = fin;
            }

            intervals.add(new Interval(date_debut_periode, date_fin_periode));

            date_debut_periode = date_fin_periode.plusSeconds(1);
            
        }

        return intervals;


    }

    private void saveCampaign_periode(Campaign campaign) throws Exception{
        
        BigDecimal budget_periode = traitement_budget(campaign);
        Long objectif_vue_periode = objectif_vue_periode(campaign);
        List<Interval> dates_periode = getDatesPeriod(campaign);
        
        try {
            List<Campaign_periode> campaignPeriodes = new ArrayList<>();
            for (int periode = 0; periode < dates_periode.size(); periode++) {
                Campaign_periode campaign_periode = new Campaign_periode();
                campaign_periode.setOrdre((long)periode);
                campaign_periode.setStart_date(dates_periode.get(periode).getStart());
                campaign_periode.setEnd_date(dates_periode.get(periode).getEnd());
                campaign_periode.setBudget_periode(budget_periode);
                campaign_periode.setVue_objectif(objectif_vue_periode);
                campaign_periode.setCampaign(campaign);

                campaignPeriodes.add(campaign_periode);
            }


            campaign_periodeRepository.saveAll(campaignPeriodes);

        } catch (Exception e) {
            throw new Exception("erreur lors de la traitement de sauvegarde de campaign periode",e);        
        }


    }


    public List<Campaign_periode> findAllCampaign_periodesByCampaign(Long id_campaign){
        return campaign_periodeRepository.findByCampaignId(id_campaign);
    }

    



   

}
