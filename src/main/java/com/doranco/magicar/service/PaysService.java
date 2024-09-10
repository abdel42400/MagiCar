package com.doranco.magicar.service;

// Importations nécessaires
import com.doranco.magicar.model.Pays;
import com.doranco.magicar.model.PaysEnum;
import com.doranco.magicar.repository.PaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Indique que cette classe est un service Spring
@Service
public class PaysService {

    // Dépendance injectée pour l'accès aux données des pays
    private final PaysRepository paysRepository;

    // Constructeur avec injection de dépendances
    @Autowired
    public PaysService(PaysRepository paysRepository) {
        this.paysRepository = paysRepository;
    }

    // Méthode pour récupérer tous les pays ou les initialiser s'ils n'existent pas
    public List<Pays> getAllPays() {
        // Récupère tous les pays de la base de données
        List<Pays> paysFromDb = paysRepository.findAll();
        
        // Si la base de données est vide, initialise les pays à partir de l'énumération PaysEnum
        if (paysFromDb.isEmpty()) {
            return Arrays.stream(PaysEnum.values())
                .map(paysEnum -> {
                    Pays pays = new Pays(paysEnum);
                    return paysRepository.save(pays);
                })
                .collect(Collectors.toList());
        }
        // Sinon, retourne les pays existants
        return paysFromDb;
    }

    // Méthode pour récupérer un pays par son ID
    public Pays getPaysById(Long id) {
        return paysRepository.findById(id).orElse(null);
    }

    // Méthode pour récupérer un pays par son nom (utilisant l'énumération PaysEnum)
    public Pays getPaysByNom(PaysEnum nom) {
        return paysRepository.findByNom(nom);
    }
}