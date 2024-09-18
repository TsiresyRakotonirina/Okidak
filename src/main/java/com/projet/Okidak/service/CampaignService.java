package com.projet.Okidak.service;

import java.util.List;

import com.projet.Okidak.dto.CampaignDto;
import com.projet.Okidak.entity.Stat_video;
import com.projet.Okidak.entity.Campaign;
import com.projet.Okidak.entity.Campaign_periode;
import com.projet.Okidak.entity.Transaction;

public interface CampaignService {

    public Campaign findCampaignByName(String name);
    
    public void saveCampaign(CampaignDto campaignDto) throws Exception;

    public List<Campaign> findAllCampaigns();

    public List<Campaign_periode> findAllCampaign_periodesByCampaign(Long id_campaign);

    public void saveTrans(Transaction data);

    public Stat_video findStat_videoByIdCampaign(Long id_campaign);


}
