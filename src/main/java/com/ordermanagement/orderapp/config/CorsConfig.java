package com.ordermanagement.orderapp.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    /**
     * Configures CORS (Cross-Origin Resource Sharing) to allow requests from:
     * - Local development (localhost)
     * - Production domain (tu-dominio.com)
     * - Swagger UI
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir solicitudes desde estos orígenes
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8080",           // Local development
                "http://localhost:3000",           // Local frontend (React, etc)
                "http://127.0.0.1:8080",           // Local alternative
                "https://plumaverde.site",         // Production domain
                "https://www.plumaverde.site",     // www variant
                "http://plumaverde.site",          // HTTP (será redirigido a HTTPS)
                "http://www.plumaverde.site"       // HTTP www variant
        ));
        
        // Permitir todos los métodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // Permitir todos los headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Accept",
                "X-CSRF-TOKEN",
                "X-API-KEY"
        ));
        
        // Headers que se pueden exponer al cliente
        configuration.setExposedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Total-Count",
                "X-Page-Number"
        ));
        
        // Permitir credenciales (cookies, tokens)
        configuration.setAllowCredentials(true);
        
        // Máximo tiempo en segundos que el navegador puede cachear la respuesta preflight
        configuration.setMaxAge(3600L);
        
        // Aplicar la configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
