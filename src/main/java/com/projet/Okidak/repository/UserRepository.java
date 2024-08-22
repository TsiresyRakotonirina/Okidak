package com.projet.Okidak.repository;

import com.projet.Okidak.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>  {

    User findByEmail(String email);

}
