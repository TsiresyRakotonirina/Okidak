package com.projet.Okidak.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.User;
import com.projet.Okidak.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", "",
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model, Principal principal){
        String emailUser = principal.getName();
        UserDto utilisateur = userService.findUserDtoByEmail(emailUser);
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("nameUser", utilisateur);
        model.addAttribute("users", users);
        return "users";
    }

    // handler method to handle home
    @GetMapping("/home")
    public String hom(Model model,Principal principal){
        String emailUser = principal.getName();
        User utilisateur = userService.findUserByEmail(emailUser);
        model.addAttribute("nameUser", utilisateur);
        return "home";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }

}