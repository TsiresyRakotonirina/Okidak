package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Campaign;


public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Campaign findByName(String name);


    
} 
