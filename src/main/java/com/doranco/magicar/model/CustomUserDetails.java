package com.doranco.magicar.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

// Cette classe étend la classe User de Spring Security pour ajouter des fonctionnalités personnalisées
public class CustomUserDetails extends User {
    
    // Identifiant de version pour la sérialisation
    private static final long serialVersionUID = 1L;
    
    // Champ supplémentaire pour stocker le nom complet de l'utilisateur
    private final String fullName;

    // Constructeur personnalisé
    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String fullName) {
        // Appel du constructeur de la classe parente (User)
        super(username, password, authorities);
        // Initialisation du champ fullName
        this.fullName = fullName;
    }

    // Getter pour récupérer le nom complet de l'utilisateur
    public String getFullName() {
        return fullName;
    }
}