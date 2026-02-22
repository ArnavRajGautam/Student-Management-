package com.arnav.sms.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    /**
     * CORS Filter Bean
     *
     * WHY: Frontend ko backend access dene ke liye
     * - Frontend: localhost:5500 (Live Server)
     * - Backend: localhost:8080 (Spring Boot)
     * - Different origins = CORS issue
     */
    @Bean
    public CorsFilter corsFilter() {
        // URL-based CORS configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // CORS configuration object
        CorsConfiguration config = new CorsConfiguration();

        // ============================================
        // ALLOW CREDENTIALS
        // ============================================
        // Cookies aur authentication headers allow karo
        config.setAllowCredentials(true);

        // ============================================
        // ALLOWED ORIGINS
        // ============================================
        // Kaun kaun se domains/ports se access allowed hai

        // Development ke liye (UNSAFE - Production mein mat use karo)
        config.setAllowedOriginPatterns(Arrays.asList("*"));

        // Production ke liye (SAFE - Specific origins)
        // config.setAllowedOrigins(Arrays.asList(
        //     "http://localhost:3000",           // React dev server
        //     "http://localhost:5500",           // VS Code Live Server
        //     "https://yourdomain.com",          // Production frontend
        //     "https://www.yourdomain.com"
        // ));

        // ============================================
        // ALLOWED HEADERS
        // ============================================
        // Request mein kaunse headers allowed hain
        config.setAllowedHeaders(Arrays.asList(
                "Origin",                          // Request origin
                "Content-Type",                    // JSON, XML, etc.
                "Accept",                          // Response type
                "Authorization",                   // Auth tokens (JWT)
                "Access-Control-Request-Method",   // Preflight request method
                "Access-Control-Request-Headers",  // Preflight request headers
                "X-Requested-With",                // AJAX requests
                "X-Custom-Header"                  // Custom headers if needed
        ));

        // ============================================
        // EXPOSED HEADERS
        // ============================================
        // Response mein kaunse headers browser ko visible honge
        config.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"              // File downloads ke liye
        ));

        // ============================================
        // ALLOWED METHODS
        // ============================================
        // Kaunse HTTP methods allowed hain
        config.setAllowedMethods(Arrays.asList(
                "GET",      // Read operations
                "POST",     // Create operations
                "PUT",      // Full update
                "PATCH",    // Partial update
                "DELETE",   // Delete operations
                "OPTIONS"   // Preflight requests
        ));

        // ============================================
        // MAX AGE
        // ============================================
        // Preflight request cache time (1 hour)
        // Browser 1 ghante tak preflight request repeat nahi karega
        config.setMaxAge(3600L);

        // ============================================
        // REGISTER CONFIGURATION
        // ============================================
        // Sabhi /api/** endpoints pe ye configuration apply karo
        source.registerCorsConfiguration("/api/**", config);

        // CORS filter return karo
        return new CorsFilter(source);
    }
}
