package com.doranco.magicar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Définit cette classe comme un contrôleur Spring
@Controller
public class HomeController {

    // Gère les requêtes GET pour la racine "/"
    @GetMapping("/")
    public String index() {
        // Retourne le nom de la vue pour la page d'accueil principale
        return "index";
    }

    // Gère les requêtes GET pour "/home"
    @GetMapping("/home")
    public String home() {
        // Retourne le nom de la vue pour la page d'accueil (possiblement pour les utilisateurs connectés)
        return "home";
    }
}