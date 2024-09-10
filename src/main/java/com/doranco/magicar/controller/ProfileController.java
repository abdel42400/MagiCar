// Importation des classes nécessaires
package com.doranco.magicar.controller;

import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// Annotation indiquant que cette classe est un contrôleur Spring
@Controller
// Mapping de base pour toutes les routes de ce contrôleur
@RequestMapping("/profile")
public class ProfileController {

    // Création d'un logger pour cette classe
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    // Injection de dépendance pour le service utilisateur
    @Autowired
    private UtilisateurService utilisateurService;

    // Injection de dépendance pour l'encodeur de mot de passe
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Méthode pour afficher le profil de l'utilisateur
    @GetMapping
    public String showProfile(Model model, Principal principal) {
        // Récupère le nom d'utilisateur (email) de l'utilisateur connecté
        String username = principal.getName();
        // Récupère l'utilisateur à partir de son email
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(username);
        
        if (utilisateur != null) {
            // Ajoute l'utilisateur au modèle pour l'affichage dans la vue
            model.addAttribute("utilisateur", utilisateur);
            return "profile"; // Renvoie vers la page de profil
        } else {
            // Redirige vers une page d'accès refusé si l'utilisateur n'est pas trouvé
            return "redirect:/access-denied";
        }
    }

    // Méthode pour mettre à jour le profil de l'utilisateur
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Utilisateur utilisateur, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        logger.info("Tentative de mise à jour du profil pour l'utilisateur : {}", username);

        // Récupère l'utilisateur actuel à partir de son email
        Utilisateur currentUser = utilisateurService.getUtilisateurByEmail(username);
        
        if (currentUser == null) {
            // Gère le cas où l'utilisateur n'est pas trouvé
            logger.error("Utilisateur non trouvé pour l'email : {}", username);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la récupération du profil utilisateur.");
            return "redirect:/profile";
        }

        // Vérifie que l'ID de l'utilisateur correspond à celui fourni dans le formulaire
        if (!currentUser.getId().equals(utilisateur.getId())) {
            logger.error("Tentative de mise à jour non autorisée. ID utilisateur : {}, ID fourni : {}", currentUser.getId(), utilisateur.getId());
            redirectAttributes.addFlashAttribute("errorMessage", "Vous n'êtes pas autorisé à modifier ce profil.");
            return "redirect:/profile";
        }

        try {
            // Logs pour suivre les modifications
            logger.info("Mise à jour du profil. Ancienne adresse : {}, Nouvelle adresse : {}", currentUser.getAdresse(), utilisateur.getAdresse());
            logger.info("Mise à jour du profil. Ancien numéro : {}, Nouveau numéro : {}", currentUser.getNumero(), utilisateur.getNumero());
            
            // Met à jour uniquement l'adresse et le numéro de téléphone
            currentUser.setAdresse(utilisateur.getAdresse());
            currentUser.setNumero(utilisateur.getNumero());
            
            // Sauvegarde les modifications
            utilisateurService.updateUtilisateur(currentUser);
            logger.info("Profil mis à jour avec succès pour l'utilisateur : {}", username);
            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");
        } catch (RuntimeException e) {
            // Gère les erreurs lors de la mise à jour
            logger.error("Erreur lors de la mise à jour du profil pour l'utilisateur : {}", username, e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile";
    }

    // Méthode pour changer le mot de passe de l'utilisateur
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword, 
                                 @RequestParam String newPassword, 
                                 @RequestParam String confirmPassword, 
                                 Principal principal, 
                                 RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(username);
        
        if (utilisateur != null) {
            // Vérifie que le mot de passe actuel est correct
            if (!passwordEncoder.matches(currentPassword, utilisateur.getMotDePasse())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mot de passe actuel incorrect");
                return "redirect:/profile";
            }
            
            // Vérifie que le nouveau mot de passe et sa confirmation correspondent
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le nouveau mot de passe et la confirmation ne correspondent pas");
                return "redirect:/profile";
            }
            
            // Encode et enregistre le nouveau mot de passe
            utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
            utilisateurService.updateUtilisateur(utilisateur);
            
            redirectAttributes.addFlashAttribute("successMessage", "Mot de passe changé avec succès");
            return "redirect:/profile";
        } else {
            // Redirige vers une page d'accès refusé si l'utilisateur n'est pas trouvé
            return "redirect:/access-denied";
        }
    }
}