package com.projet.Okidak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet.Okidak.entity.Transaction;
import com.projet.Okidak.service.CampaignService;

@RestController
@RequestMapping("/api/trans")
public class TransRESTController {
    
    @Autowired
    private CampaignService campaignService;

    // Méthode pour l'API REST qui renvoie des données JSON
    @PostMapping("/save")
    public ResponseEntity<Void> saveTransaction(@RequestBody Transaction request) {
        try {
            campaignService.saveTrans(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
