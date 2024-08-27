package com.projet.Okidak.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projet.Okidak.entity.Campaign;
import com.projet.Okidak.repository.CampaignRepository;
import com.projet.Okidak.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService{
    
    private CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository){
        this.campaignRepository = campaignRepository;
    }

    @Override 
    public Campaign findCampaignByName(String name){
        return campaignRepository.findByName(name);
    }

    @Override
    public void saveCampaign(Campaign campaign){
        campaignRepository.save(campaign);
    }

    @Override
    public List<Campaign> findAllCampaigns(){
        return campaignRepository.findAll();
    }

}
