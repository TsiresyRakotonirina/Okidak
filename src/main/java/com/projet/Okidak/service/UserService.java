package com.projet.Okidak.service;

import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
    
}