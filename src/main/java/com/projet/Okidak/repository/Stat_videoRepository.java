package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projet.Okidak.entity.Stat_video;

public interface Stat_videoRepository extends JpaRepository<Stat_video, String> {

    Stat_video findByIdCampaign(Long idCampaign);
}