package com.projet.Okidak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.projet.Okidak.security.CustomAuthSuccessHandler;

@Configuration
@EnableWebSecurity
public class SpringSecurity{

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/register/**").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/users","/add_departement","/departement/save","/add_type_campaign","/type_campaign/save").hasRole("ADMIN")
                                .requestMatchers("/home","/add_annonceur","/annonceur/save","/add_campaign","/campaign/save","/liste_campaign").hasRole("USER")
                                // .requestMatchers("/page1", "/page2").authenticated()
                                .requestMatchers("/static/**", "/css/**", "/fonts/**","/js/**", "/images/**","/uploadDir/**").permitAll()
                                .requestMatchers("/layout").permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                // .defaultSuccessUrl("/users")
                                .successHandler(new CustomAuthSuccessHandler())
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

// @Override
// public void configure(WebSecurity web) throws Exception {
//         web
//         .ignoring()
//         .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
// }

}