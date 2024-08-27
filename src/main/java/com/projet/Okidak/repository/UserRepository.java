package com.projet.Okidak.repository;

import com.projet.Okidak.entity.User;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>  {

    User findByEmail(String email);

    // @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    // List<User> findUsersByRoleName(@Param("roleName") String roleName);

}
