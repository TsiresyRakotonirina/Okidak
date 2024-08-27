package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Annonceur;

public interface AnnonceurRepository extends JpaRepository<Annonceur, Long>{

    Annonceur findByName(String name);
    
}
