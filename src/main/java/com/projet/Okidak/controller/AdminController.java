package com.projet.Okidak.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.Departement;
import com.projet.Okidak.entity.Type_campaign;
import com.projet.Okidak.service.AdminService;
import com.projet.Okidak.service.UserService;



@Controller
public class AdminController {
    
        private UserService userService;
        private AdminService adminService;

        public AdminController(UserService userService, AdminService adminService) {
            this.userService = userService;
            this.adminService = adminService    ;
        }


        @GetMapping("/add_departement")
        public String depar(Model model,Principal principal){
            String emailUser = principal.getName();
            UserDto utilisateur = userService.findUserDtoByEmail(emailUser);
            model.addAttribute("nameUser", utilisateur);    
            model.addAttribute("departement", new Departement());
            return "ajout_departement";
        }


        @PostMapping("/departement/save")
        public String save_departement(Departement depa, BindingResult result,Model model, Principal principal){

            Departement existingDepartement = adminService.findDepartementByName(depa.getName());

            if(existingDepartement != null && existingDepartement.getName() != null && !existingDepartement.getName().isEmpty()){
                result.rejectValue("name", "",
                        "This departement already exists");
            }

            if(result.hasErrors()){
                String emailUser = principal.getName();
                UserDto utilisateur = userService.findUserDtoByEmail(emailUser);
                model.addAttribute("nameUser", utilisateur);    
                model.addAttribute("departement", depa);
                return "ajout_departement";
            }

            adminService.saveDepartement(depa);
            return "redirect:/add_departement?success";
        }


        @GetMapping("/add_type_campaign")
        public String typeCamp(Model model,Principal principal){
            String emailUser = principal.getName();
            UserDto utilisateur = userService.findUserDtoByEmail(emailUser);
            model.addAttribute("nameUser", utilisateur);    
            model.addAttribute("type_campaign", new Type_campaign());
            return "ajout_type_campaign";
        }


        @PostMapping("/type_campaign/save")
        public String save_type(Type_campaign tc, BindingResult result,Model model, Principal principal){

            Type_campaign existingType_campaign = adminService.findType_campaignByName(tc.getName());

            if(existingType_campaign != null && existingType_campaign.getName() != null && !existingType_campaign.getName().isEmpty()){
                result.rejectValue("name", "",
                        "This Type of camapaign already exists");
            }

            if(result.hasErrors()){
                // String emailUser = ((Principal) model.getAttribute("principal")).getName();
                String emailUser = principal.getName();
                UserDto utilisateur = userService.findUserDtoByEmail(emailUser);
                model.addAttribute("nameUser", utilisateur);
                model.addAttribute("type_campaign", tc);
                return "ajout_type_campaign";
            }

            adminService.saveType_campaign(tc);
            return "redirect:/add_type_campaign?success";
        }

}
