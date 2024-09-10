package com.doranco.magicar.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doranco.magicar.model.CustomUserDetails;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.repository.UtilisateurRepository;

// Indique que cette classe est un service Spring
@Service
// Implémente l'interface UserDetailsService de Spring Security
public class CustomUserDetailsService implements UserDetailsService {

    // Dépendance pour accéder aux données des utilisateurs
    private final UtilisateurRepository utilisateurRepository;

    // Constructeur pour l'injection de dépendance
    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // Méthode requise par l'interface UserDetailsService
    // Elle est appelée par Spring Security lors de l'authentification
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Recherche de l'utilisateur dans la base de données par son email (username)
        Utilisateur utilisateur = utilisateurRepository.findByMail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Création et retour d'un objet CustomUserDetails
        return new CustomUserDetails(
            utilisateur.getMail(), // Utilise l'email comme nom d'utilisateur
            utilisateur.getMotDePasse(), // Mot de passe encodé
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole())), // Convertit le rôle en autorité
            utilisateur.getPrenom() + " " + utilisateur.getNom() // Nom complet de l'utilisateur
        );
    }
}