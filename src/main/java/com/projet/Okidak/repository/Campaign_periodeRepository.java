package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Campaign_periode;

public interface Campaign_periodeRepository extends JpaRepository<Campaign_periode, Long> {

    Campaign_periode findByName(String name);

} 
