package com.projet.Okidak.service;

import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.Annonceur;
import com.projet.Okidak.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    UserDto findUserDtoByEmail(String email);

    List<UserDto> findAllUsers();

    Annonceur findAnnonceurByName(String name);

    void saveAnnonceur(Annonceur annonceur);

    List<Annonceur> findAllAnnonceurs();

    
}