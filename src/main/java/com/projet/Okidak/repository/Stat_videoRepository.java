package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projet.Okidak.entity.Stat_video;

import java.util.List;

public interface Stat_videoRepository extends JpaRepository<Stat_video, String> {

    List<Stat_video> findByIdCampaign(Long idCampaign);
}