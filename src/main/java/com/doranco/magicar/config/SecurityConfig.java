package com.doranco.magicar.config;

// Importations nécessaires
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.doranco.magicar.service.CustomUserDetailsService;

// Indique que cette classe est une configuration Spring
@Configuration
// Active la sécurité web de Spring Security
@EnableWebSecurity
public class SecurityConfig {

    // Service personnalisé pour charger les détails des utilisateurs
    private final CustomUserDetailsService userDetailsService;

    // Constructeur pour l'injection de dépendances
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Configuration de la chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuration des autorisations pour les requêtes HTTP
            .authorizeHttpRequests((requests) -> requests
                // Permet l'accès public à certaines URLs
                .requestMatchers("/", "/index", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Restreint l'accès aux URLs commençant par "/admin/" aux utilisateurs avec le rôle ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            // Configuration du formulaire de login
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            // Configuration de la déconnexion
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Gestion des exceptions d'accès
            .exceptionHandling((exceptions) -> exceptions
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }

    // Définition de l'encodeur de mot de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuration du fournisseur d'authentification
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}