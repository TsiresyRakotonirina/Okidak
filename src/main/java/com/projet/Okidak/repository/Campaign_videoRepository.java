package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Campaign_video;

public interface Campaign_videoRepository extends JpaRepository<Campaign_video,Long> {

    Campaign_video findByName(String name);
    
}
