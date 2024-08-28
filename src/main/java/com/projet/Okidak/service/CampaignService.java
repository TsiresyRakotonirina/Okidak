package com.projet.Okidak.service;

import java.util.List;

import com.projet.Okidak.dto.CampaignDto;
import com.projet.Okidak.entity.Campaign;

public interface CampaignService {

    public Campaign findCampaignByName(String name);
    
    public void saveCampaign(CampaignDto campaignDto);

    public List<Campaign> findAllCampaigns();
}
