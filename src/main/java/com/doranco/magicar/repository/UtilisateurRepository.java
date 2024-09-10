package com.doranco.magicar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.magicar.model.Utilisateur;

// Cette interface est un repository Spring Data JPA pour l'entité Utilisateur
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    // Trouve un utilisateur par son adresse email
    // Retourne un Optional pour gérer le cas où l'utilisateur n'existe pas
    Optional<Utilisateur> findByMail(String mail);
    
    // Vérifie si un utilisateur avec l'adresse email donnée existe
    boolean existsByMail(String mail);
    
    // Vérifie si un utilisateur avec l'adresse email donnée existe, 
    // en excluant l'utilisateur avec l'ID spécifié
    // Utile pour vérifier l'unicité de l'email lors de la mise à jour d'un utilisateur
    boolean existsByMailAndIdNot(String mail, Long id);
}