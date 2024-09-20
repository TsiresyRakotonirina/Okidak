package com.projet.Okidak.service.impl;

import com.projet.Okidak.dto.UserDto;
import com.projet.Okidak.entity.Annonceur;
import com.projet.Okidak.entity.Role;
import com.projet.Okidak.entity.User;
import com.projet.Okidak.repository.AnnonceurRepository;
import com.projet.Okidak.repository.RoleRepository;
import com.projet.Okidak.repository.UserRepository;
import com.projet.Okidak.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AnnonceurRepository annonceurRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           AnnonceurRepository annonceurRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.annonceurRepository = annonceurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto findUserDtoByEmail(String email) {
        User user = userRepository.findByEmail(email);
        UserDto userDto = mapToUserDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }


    @Override
    public Annonceur findAnnonceurByName(String name){
        return annonceurRepository.findByName(name);
    }

    @Override 
    public void saveAnnonceur(Annonceur annonceur){
        annonceurRepository.save(annonceur);
    }

    @Override
    public List<Annonceur> findAllAnnonceurs(){
        return annonceurRepository.findAll();
    }


    

   

}