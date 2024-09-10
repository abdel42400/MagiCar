package com.doranco.magicar.service;

// Importations nécessaires
import com.doranco.magicar.model.Voiture;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.model.PaysEnum;
import com.doranco.magicar.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Indique que cette classe est un service Spring
@Service
public class VoitureService {

    // Dépendance injectée pour l'accès aux données des voitures
    private final VoitureRepository voitureRepository;

    // Constructeur avec injection de dépendances
    @Autowired
    public VoitureService(VoitureRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    // Méthode pour sauvegarder une nouvelle voiture avec gestion de photo
    @Transactional
    public Voiture saveVoiture(Voiture voiture, MultipartFile photoFile) throws IOException {
        // Si une photo est fournie, l'ajoute à la voiture
        if (photoFile != null && !photoFile.isEmpty()) {
            voiture.setPhoto(photoFile.getBytes());
            voiture.setPhotoContentType(photoFile.getContentType());
        }
        // Sauvegarde et retourne la voiture
        return voitureRepository.save(voiture);
    }

    // Méthode pour récupérer toutes les voitures
    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    // Méthode pour récupérer les voitures d'un utilisateur spécifique
    public List<Voiture> getVoituresByUtilisateur(Utilisateur utilisateur) {
        return voitureRepository.findByUtilisateur(utilisateur);
    }
    
    // Méthode pour récupérer une voiture par son ID
    public Optional<Voiture> getVoitureById(Long id) {
        return voitureRepository.findById(id);
    }

    // Méthode pour mettre à jour une voiture existante
    @Transactional
    public Voiture updateVoiture(Long id, Voiture voitureDetails, MultipartFile photoFile) throws IOException {
        // Récupère la voiture existante ou lance une exception si non trouvée
        Voiture voiture = getVoitureById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
        
        // Met à jour les détails de la voiture
        voiture.setMarque(voitureDetails.getMarque());
        voiture.setModele(voitureDetails.getModele());
        voiture.setPrix(voitureDetails.getPrix());
        voiture.setCarburant(voitureDetails.getCarburant());
        voiture.setKilometrage(voitureDetails.getKilometrage());
        voiture.setDescription(voitureDetails.getDescription());
        voiture.setPays(voitureDetails.getPays());
        
        // Si une nouvelle photo est fournie, la met à jour
        if (photoFile != null && !photoFile.isEmpty()) {
            voiture.setPhoto(photoFile.getBytes());
            voiture.setPhotoContentType(photoFile.getContentType());
        }
        
        // Sauvegarde et retourne la voiture mise à jour
        return voitureRepository.save(voiture);
    }

    // Méthode pour supprimer une voiture
    @Transactional
    public void deleteVoiture(Long id) {
        voitureRepository.deleteById(id);
    }
    
    // Méthode pour filtrer les voitures selon divers critères
    public List<Voiture> getVoituresFiltrees(Double prixMin, Double prixMax, Integer kmMin, Integer kmMax, PaysEnum pays) {
        // Récupère toutes les voitures
        List<Voiture> voitures = voitureRepository.findAll();
        
        // Applique les filtres 
        return voitures.stream()
            .filter(v -> prixMin == null || v.getPrix() >= prixMin)
            .filter(v -> prixMax == null || v.getPrix() <= prixMax)
            .filter(v -> kmMin == null || v.getKilometrage() >= kmMin)
            .filter(v -> kmMax == null || v.getKilometrage() <= kmMax)
            .filter(v -> pays == null || (v.getPays() != null && v.getPays().getNom() == pays))
            .collect(Collectors.toList());
    }
}