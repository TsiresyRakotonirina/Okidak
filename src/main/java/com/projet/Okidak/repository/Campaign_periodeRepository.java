package com.projet.Okidak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Campaign_periode;

public interface Campaign_periodeRepository extends JpaRepository<Campaign_periode, Long> {

    public List<Campaign_periode> findByCampaignId(Long id_campaign);

} 
