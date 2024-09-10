package com.doranco.magicar.repository;

import com.doranco.magicar.model.Voiture;
import com.doranco.magicar.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Cette interface est un repository Spring Data JPA pour l'entité Voiture
@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    
    // Cette méthode permet de trouver toutes les voitures appartenant à un utilisateur spécifique
    // Spring Data JPA implémentera automatiquement cette méthode basée sur son nom
    List<Voiture> findByUtilisateur(Utilisateur utilisateur);
}