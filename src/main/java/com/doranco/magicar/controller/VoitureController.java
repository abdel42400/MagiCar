package com.doranco.magicar.controller;

import com.doranco.magicar.model.Voiture;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.model.PaysEnum;
import com.doranco.magicar.service.VoitureService;
import com.doranco.magicar.service.UtilisateurService;
import com.doranco.magicar.service.PaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

// Définit cette classe comme un contrôleur Spring
@Controller
// Toutes les routes de ce contrôleur commenceront par "/voitures"
@RequestMapping("/voitures")
public class VoitureController {

    // Déclaration des services nécessaires
    private final VoitureService voitureService;
    private final UtilisateurService utilisateurService;
    private final PaysService paysService;

    // Injection de dépendances via le constructeur
    @Autowired
    public VoitureController(VoitureService voitureService, UtilisateurService utilisateurService, PaysService paysService) {
        this.voitureService = voitureService;
        this.utilisateurService = utilisateurService;
        this.paysService = paysService;
    }

    // Gère les requêtes GET pour "/voitures" avec des filtres optionnels
    @GetMapping
    public String afficherListeVoitures(
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(required = false) Integer kmMin,
            @RequestParam(required = false) Integer kmMax,
            @RequestParam(required = false) String pays,
            Model model) {
        PaysEnum paysEnum = null;
        if (pays != null && !pays.isEmpty()) {
            try {
                paysEnum = PaysEnum.valueOf(pays.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si le pays n'est pas valide, on ajoute un message d'erreur
                model.addAttribute("paysError", "Pays non valide sélectionné");
            }
        }
        // Récupère la liste des voitures filtrées
        List<Voiture> voitures = voitureService.getVoituresFiltrees(prixMin, prixMax, kmMin, kmMax, paysEnum);
        model.addAttribute("voitures", voitures);
        return "voitures";
    }

    // Gère les requêtes GET pour "/voitures/publier"
    @GetMapping("/publier")
    public String afficherFormulairePublication(Model model) {
        // Ajoute un nouvel objet Voiture et la liste des pays au modèle
        model.addAttribute("voiture", new Voiture());
        model.addAttribute("pays", paysService.getAllPays());
        return "publication-voiture";
    }

    // Gère les requêtes GET pour "/voitures/mes-annonces"
    @GetMapping("/mes-annonces")
    public String afficherMesAnnonces(Model model, Principal principal) {
        // Récupère l'utilisateur connecté
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
        // Récupère les voitures de l'utilisateur
        List<Voiture> mesVoitures = voitureService.getVoituresByUtilisateur(utilisateur);
        model.addAttribute("voitures", mesVoitures);
        return "mes-annonces";
    }

    // Gère les requêtes POST pour "/voitures/publier"
    @PostMapping("/publier")
    public String publierVoiture(@ModelAttribute @Valid Voiture voiture,
                                 BindingResult result,
                                 @RequestParam("photoFile") MultipartFile photoFile,
                                 Principal principal,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pays", paysService.getAllPays());
            return "publication-voiture";
        }

        try {
            // Récupère l'utilisateur connecté
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            voiture.setUtilisateur(utilisateur);
            // Sauvegarde la voiture avec la photo
            @SuppressWarnings("unused")
            Voiture savedVoiture = voitureService.saveVoiture(voiture, photoFile);
            return "redirect:/voitures";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la publication de la voiture : " + e.getMessage());
            model.addAttribute("pays", paysService.getAllPays());
            return "publication-voiture";
        }
    }

    // Gère les requêtes GET pour "/voitures/image/{id}"
    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Voiture voiture = voitureService.getVoitureById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
        
        // Retourne l'image de la voiture
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(voiture.getPhotoContentType()))
                .body(voiture.getPhoto());
    }

    // Gère les requêtes GET pour "/voitures/modifier/{id}"
    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model, Principal principal) {
        Voiture voiture = voitureService.getVoitureById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
        
        // Vérifie que l'utilisateur connecté est bien le propriétaire de l'annonce
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
        if (!voiture.getUtilisateur().equals(utilisateur)) {
            return "redirect:/access-denied";
        }
        
        model.addAttribute("voiture", voiture);
        model.addAttribute("pays", paysService.getAllPays());
        return "modification-voiture";
    }

    // Gère les requêtes POST pour "/voitures/modifier/{id}"
    @PostMapping("/modifier/{id}")
    public String modifierVoiture(@PathVariable Long id, 
                                  @ModelAttribute @Valid Voiture voiture,
                                  BindingResult result,
                                  @RequestParam("photoFile") MultipartFile photoFile,
                                  Principal principal,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pays", paysService.getAllPays());
            return "modification-voiture";
        }
        
        try {
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            Voiture existingVoiture = voitureService.getVoitureById(id)
                    .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
            
            // Vérifie que l'utilisateur connecté est bien le propriétaire de l'annonce
            if (!existingVoiture.getUtilisateur().equals(utilisateur)) {
                return "redirect:/access-denied";
            }
            
            voiture.setUtilisateur(utilisateur);
            voitureService.updateVoiture(id, voiture, photoFile);
            return "redirect:/voitures/mes-annonces";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour de la voiture : " + e.getMessage());
            model.addAttribute("pays", paysService.getAllPays());
            return "modification-voiture";
        }
    }

    // Gère les requêtes GET pour "/voitures/supprimer/{id}"
    @GetMapping("/supprimer/{id}")
    public String supprimerVoiture(@PathVariable Long id, Principal principal) {
        try {
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            Voiture voiture = voitureService.getVoitureById(id)
                    .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
            
            // Vérifie que l'utilisateur connecté est bien le propriétaire de l'annonce
            if (!voiture.getUtilisateur().equals(utilisateur)) {
                return "redirect:/access-denied";
            }
            
            voitureService.deleteVoiture(id);
            return "redirect:/voitures/mes-annonces";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}