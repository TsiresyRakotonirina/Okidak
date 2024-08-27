package com.projet.Okidak.service;

import java.util.List;

import com.projet.Okidak.entity.Departement;
import com.projet.Okidak.entity.Type_campaign;

public interface AdminService {

    void saveDepartement(Departement dep);

    Departement findDepartementByName(String name);

    List<Departement> findAllDepartements();

    void saveType_campaign(Type_campaign typ);
    
    Type_campaign findType_campaignByName(String name);

    List<Type_campaign> findAllType_campaign();
} 