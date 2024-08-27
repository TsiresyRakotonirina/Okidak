package com.projet.Okidak.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Type_campaign;

public interface Type_campaignRepository extends JpaRepository<Type_campaign, Long>{

    Type_campaign findByName(String name);
    
}
