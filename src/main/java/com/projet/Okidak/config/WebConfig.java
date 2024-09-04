// package com.projet.Okidak.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import org.springframework.lang.NonNull;


// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/uploadDir/**")
//                 .addResourceLocations("file:src/main/resources/uploadDir/");

        
//     }

//     @Bean
//     public WebMvcConfigurer corsConfigurer() {
//         return new WebMvcConfigurer() {
//             @Override
//             public void addCorsMappings(@NonNull CorsRegistry registry) {
//                 registry.addMapping("/uploadDir/**")
//                         .allowedOrigins("http://localhost:8080")
//                         .allowedMethods("GET");
                

//             }
//         };
//     }
// }
