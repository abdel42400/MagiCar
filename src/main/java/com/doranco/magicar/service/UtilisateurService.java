package com.doranco.magicar.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.repository.UtilisateurRepository;

// Indique que cette classe est un service Spring
@Service
// Indique que toutes les méthodes de cette classe sont transactionnelles
@Transactional
public class UtilisateurService {

    // Dépendances injectées
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    // Regex pour la validation du mot de passe
    private static final String REGEX_MOT_DE_PASSE = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=.*\\d)(?!.*['\"*;])(?!.*--)(?!.*\\/\\*)(?!.*\\*\\/)(?!.*union)(?!.*select)(?!.*insert)(?!.*delete)(?!.*update).{8,}$";
    private static final Pattern PATTERN_MOT_DE_PASSE = Pattern.compile(REGEX_MOT_DE_PASSE);

    // Constructeur avec injection de dépendances
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Méthode pour sauvegarder un nouvel utilisateur
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        // Vérifie si l'email existe déjà
        if (utilisateurRepository.existsByMail(utilisateur.getMail())) {
            throw new RuntimeException("Cet e-mail est déjà utilisé.");
        }
        // Vérifie si le mot de passe est valide
        if (!estMotDePasseValide(utilisateur.getMotDePasse())) {
            throw new RuntimeException("Le mot de passe ne respecte pas les critères de sécurité.");
        }
        // Encode le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        // Définit le rôle par défaut
        utilisateur.setRole("CLIENT");
        // Sauvegarde l'utilisateur
        return utilisateurRepository.save(utilisateur);
    }

    // Méthode pour vérifier si un mot de passe est valide
    public boolean estMotDePasseValide(String motDePasse) {
        return PATTERN_MOT_DE_PASSE.matcher(motDePasse).matches();
    }

    // Méthode pour trouver un utilisateur par son email
    public Utilisateur findByMail(String mail) {
        return utilisateurRepository.findByMail(mail).orElse(null);
    }

    // Méthode pour récupérer tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Méthode pour récupérer un utilisateur par son ID
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    // Méthode pour supprimer un utilisateur
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }

    // Méthode pour vérifier si un email est unique
    public boolean isEmailUnique(String email, Long userId) {
        return !utilisateurRepository.existsByMailAndIdNot(email, userId);
    }

    // Méthode pour mettre à jour un utilisateur
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        Utilisateur existingUtilisateur = utilisateurRepository.findById(utilisateur.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Met à jour l'adresse et le numéro de téléphone
        existingUtilisateur.setAdresse(utilisateur.getAdresse());
        existingUtilisateur.setNumero(utilisateur.getNumero());

        // Met à jour le mot de passe si nécessaire
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty() && !utilisateur.getMotDePasse().startsWith("$2a$")) {
            if (!estMotDePasseValide(utilisateur.getMotDePasse())) {
                throw new RuntimeException("Le nouveau mot de passe ne respecte pas les critères de sécurité.");
            }
            existingUtilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        }

        return utilisateurRepository.save(existingUtilisateur);
    }

    // Méthode pour vérifier si un email existe
    public boolean existsByMail(String mail) {
        return utilisateurRepository.existsByMail(mail);
    }

    // Méthode pour trouver un utilisateur par email pour l'authentification
    public Utilisateur findByMailForAuthentication(String mail) {
        return utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Méthode pour vérifier un mot de passe
    public boolean checkPassword(Utilisateur utilisateur, String rawPassword) {
        return passwordEncoder.matches(rawPassword, utilisateur.getMotDePasse());
    }

    // Méthode pour récupérer un utilisateur par email
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + email));
    }

    // Méthode pour changer le mot de passe d'un utilisateur
    public void changerMotDePasse(Utilisateur utilisateur, String motDePasseActuel, String nouveauMotDePasse, String confirmationMotDePasse) {
        // Vérifie si le mot de passe actuel est correct
        if (!passwordEncoder.matches(motDePasseActuel, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Le mot de passe actuel est incorrect.");
        }
        
        // Vérifie si le nouveau mot de passe et sa confirmation correspondent
        if (!nouveauMotDePasse.equals(confirmationMotDePasse)) {
            throw new RuntimeException("Le nouveau mot de passe et sa confirmation ne correspondent pas.");
        }
        
        // Vérifie si le nouveau mot de passe est valide
        if (!estMotDePasseValide(nouveauMotDePasse)) {
            throw new RuntimeException("Le nouveau mot de passe ne respecte pas les critères de sécurité.");
        }
        
        // Encode et sauvegarde le nouveau mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);
    }
}