package com.projet.Okidak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet.Okidak.entity.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Long>{

    Departement findByName(String name);
    
}
