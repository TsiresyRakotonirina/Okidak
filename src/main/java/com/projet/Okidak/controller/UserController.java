package com.projet.Okidak.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projet.Okidak.dto.CampaignDto;
import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.Annonceur;
import com.projet.Okidak.entity.Campaign;
import com.projet.Okidak.entity.Campaign_periode;
import com.projet.Okidak.entity.Departement;
import com.projet.Okidak.entity.Type_campaign;
import com.projet.Okidak.entity.User;
import com.projet.Okidak.service.AdminService;
import com.projet.Okidak.service.CampaignService;
import com.projet.Okidak.service.UserService;



@Controller
public class UserController {

    private UserService userService;
    private AdminService adminService;
    private CampaignService campaignService;


    public UserController(UserService userService, AdminService adminService, CampaignService campaignService) {
        this.userService = userService;
        this.adminService = adminService;
        this.campaignService = campaignService;
    }


    // handler method to handle ajout_campaign
    @GetMapping("/add_annonceur")
    public String add_annonceur(Model model,Principal principal){
        String emailUser = principal.getName();
        User utilisateur = userService.findUserByEmail(emailUser);
        UserDto userDto = userService.findUserDtoByEmail(emailUser);
        List<Departement> departements = adminService.findAllDepartements();
        model.addAttribute("nameUser", utilisateur);
        model.addAttribute("user", userDto);
        model.addAttribute("annonceur", new Annonceur());
        model.addAttribute("departements", departements);
        return "ajout_annonceur";
    }


    @PostMapping("/annonceur/save")
        public String save_annonceur(Annonceur annonceur, BindingResult result,Model model, Principal principal){

            Annonceur existingAnnonceur = userService.findAnnonceurByName(annonceur.getName());

            if(existingAnnonceur != null && existingAnnonceur.getName() != null && !existingAnnonceur.getName().isEmpty()){
                result.rejectValue("name", "",
                        "This announcer already exists");
            }

            if(result.hasErrors()){
                String emailUser = principal.getName();
                User utilisateur = userService.findUserByEmail(emailUser);
                UserDto userDto = userService.findUserDtoByEmail(emailUser);
                List<Departement> departements = adminService.findAllDepartements();
                model.addAttribute("nameUser", utilisateur);
                model.addAttribute("user", userDto);
                model.addAttribute("annonceur", annonceur);
                model.addAttribute("departements", departements);
                return "ajout_annonceur";
            }

            userService.saveAnnonceur(annonceur);
            return "redirect:/add_annonceur?success";
    }



    // handler method to handle ajout_campaign
    @GetMapping("/add_campaign")
    public String camp(Model model,Principal principal){
        String emailUser = principal.getName();
        User utilisateur = userService.findUserByEmail(emailUser);
        UserDto userDto = userService.findUserDtoByEmail(emailUser);
        List<Type_campaign> type = adminService.findAllType_campaign();
        List<Annonceur> annonceurs = userService.findAllAnnonceurs();
        CampaignDto campaignDto = new CampaignDto();
        campaignDto.setType(null);
        campaignDto.setAnnonceur(null);
        model.addAttribute("nameUser", utilisateur);
        model.addAttribute("user", userDto);
        model.addAttribute("campaignDto", campaignDto);
        model.addAttribute("campaignTypes", type);
        model.addAttribute("annonceurs", annonceurs);
        return "ajout_campaign";
    }


    @PostMapping("/campaign/save")
        public String save_campaign(CampaignDto campaignDto, BindingResult result,Model model, Principal principal) throws Exception{

            Campaign existingCampaign = campaignService.findCampaignByName(campaignDto.getName());
                
            if(existingCampaign != null && existingCampaign.getName() != null && !existingCampaign.getName().isEmpty()){
                result.rejectValue("name", "",
                        "This campaign already exists");
            }

            if(result.hasErrors()){
                String emailUser = principal.getName();
                User utilisateur = userService.findUserByEmail(emailUser);
                UserDto userDto = userService.findUserDtoByEmail(emailUser);
                List<Type_campaign> type = adminService.findAllType_campaign();
                List<Annonceur> annonceurs = userService.findAllAnnonceurs();
                model.addAttribute("nameUser", utilisateur);
                model.addAttribute("user", userDto);
                model.addAttribute("campaign", campaignDto);
                model.addAttribute("campaignTypes", type);
                model.addAttribute("annonceurs", annonceurs);
                model.addAttribute("erreur", result.getAllErrors());
                return "ajout_campaign";
            }

            campaignService.saveCampaign(campaignDto);
            return "redirect:/add_campaign?success";
    }

    @GetMapping("/liste_campaign")
    public String listcamp(Model model,Principal principal){
        String emailUser = principal.getName();
        User utilisateur = userService.findUserByEmail(emailUser);
        UserDto userDto = userService.findUserDtoByEmail(emailUser);
        List<Campaign> campaigns = campaignService.findAllCampaigns();

        model.addAttribute("nameUser", utilisateur);
        model.addAttribute("user", userDto);
        model.addAttribute("campaigns", campaigns);

        return "liste_campaign";

    }


    @GetMapping("/liste_campaign_periode")
    public String listcampPeriod(@RequestParam("id_campaign") Long idCampaign, Model model,Principal principal){
        String emailUser = principal.getName();
        User utilisateur = userService.findUserByEmail(emailUser);
        UserDto userDto = userService.findUserDtoByEmail(emailUser);
        List<Campaign_periode> campaign_periodes = campaignService.findAllCampaign_periodesByCampaign(idCampaign);
        model.addAttribute("nameUser", utilisateur);
        model.addAttribute("user", userDto);
        model.addAttribute("campaign_periodes", campaign_periodes);
        return "liste_campaign_periode";
    }

    
}

