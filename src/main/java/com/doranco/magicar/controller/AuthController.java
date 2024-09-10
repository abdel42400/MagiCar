package com.doranco.magicar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.service.UtilisateurService;
import jakarta.validation.Valid;

// Définit cette classe comme un contrôleur Spring
@Controller
public class AuthController {

    // Injection de dépendance du service utilisateur
    @Autowired
    private UtilisateurService utilisateurService;

    // Gère les requêtes GET pour "/login"
    @GetMapping("/login")
    public String login() {
        // Retourne le nom de la vue pour la page de connexion
        return "login";
    }

    // Gère les requêtes GET pour "/register"
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Ajoute un nouvel objet Utilisateur au modèle pour le formulaire
        model.addAttribute("utilisateur", new Utilisateur());
        // Retourne le nom de la vue pour le formulaire d'inscription
        return "register";
    }

    // Gère les requêtes POST pour "/register"
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("utilisateur") @Valid Utilisateur utilisateur, 
                               BindingResult result, 
                               @RequestParam("confirmMotDePasse") String confirmMotDePasse,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        // Vérifie s'il y a des erreurs de validation
        if (result.hasErrors()) {
            return "register";
        }
        
        // Vérifie si les mots de passe correspondent
        if (!utilisateur.getMotDePasse().equals(confirmMotDePasse)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "register";
        }
        
        try {
            // Tente de sauvegarder l'utilisateur
            utilisateurService.saveUtilisateur(utilisateur);
            // Ajoute un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie. Veuillez vous connecter.");
            // Redirige vers la page de connexion
            return "redirect:/login";
        } catch (RuntimeException e) {
            // En cas d'erreur, ajoute le message d'erreur au modèle
            model.addAttribute("error", e.getMessage());
            // Retourne à la page d'inscription
            return "register";
        }
    }
}