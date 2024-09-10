package com.doranco.magicar.repository;

import com.doranco.magicar.model.Pays;
import com.doranco.magicar.model.PaysEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Cette interface est un repository Spring Data JPA pour l'entité Pays
@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
    
    // Cette méthode permet de trouver un pays par son nom (qui est un enum PaysEnum)
    // Spring Data JPA implémentera automatiquement cette méthode basée sur son nom
    Pays findByNom(PaysEnum nom);
}