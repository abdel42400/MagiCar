package com.doranco.magicar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Définit cette classe comme un contrôleur Spring
@Controller
public class ContactController {

    // Gère les requêtes GET pour "/contact"
    @GetMapping("/contact")
    public String contact() {
        // Retourne le nom de la vue pour la page de contact
        return "contact";
    }
}