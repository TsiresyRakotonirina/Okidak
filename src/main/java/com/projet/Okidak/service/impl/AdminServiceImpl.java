package com.projet.Okidak.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projet.Okidak.entity.Departement;
import com.projet.Okidak.entity.Type_campaign;
import com.projet.Okidak.repository.DepartementRepository;
import com.projet.Okidak.repository.Type_campaignRepository;
import com.projet.Okidak.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

    private DepartementRepository departementRepository;
    private Type_campaignRepository type_campaignRepository;

    public AdminServiceImpl(DepartementRepository departementRepository,
                            Type_campaignRepository type_campaignRepository){
        this.departementRepository = departementRepository;
        this.type_campaignRepository = type_campaignRepository;
    }

    @Override
    public void saveDepartement(Departement depa){
        departementRepository.save(depa);
    }

    @Override
    public Departement findDepartementByName(String name){
        return departementRepository.findByName(name);
    }

    @Override
    public List<Departement> findAllDepartements(){
        return departementRepository.findAll();
    }

    @Override
    public void saveType_campaign(Type_campaign type){
        type_campaignRepository.save(type);
    }

    @Override
    public Type_campaign findType_campaignByName(String name){
        return type_campaignRepository.findByName(name);
    }



    @Override
    public List<Type_campaign> findAllType_campaign(){
        return type_campaignRepository.findAll();
    }

}
