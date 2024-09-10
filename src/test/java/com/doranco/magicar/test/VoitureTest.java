package com.doranco.magicar.test;

import org.junit.jupiter.api.Test;

import com.doranco.magicar.model.Pays;
import com.doranco.magicar.model.PaysEnum;
import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.model.Voiture;

import static org.junit.jupiter.api.Assertions.*;

class VoitureTest {

    @Test
    void testConstructeurEtGetters() {
        Voiture voiture = new Voiture("Toyota", "Corolla", 25000.0, "Essence", 50000, "Voiture en bon état");
        
        assertEquals("Toyota", voiture.getMarque());
        assertEquals("Corolla", voiture.getModele());
        assertEquals(25000.0, voiture.getPrix());
        assertEquals("Essence", voiture.getCarburant());
        assertEquals(50000, voiture.getKilometrage());
        assertEquals("Voiture en bon état", voiture.getDescription());
    }

    @Test
    void testSetters() {
        Voiture voiture = new Voiture();
        
        voiture.setMarque("Honda");
        voiture.setModele("Civic");
        voiture.setPrix(22000.0);
        voiture.setCarburant("Diesel");
        voiture.setKilometrage(30000);
        voiture.setDescription("Très bon état");
        
        assertEquals("Honda", voiture.getMarque());
        assertEquals("Civic", voiture.getModele());
        assertEquals(22000.0, voiture.getPrix());
        assertEquals("Diesel", voiture.getCarburant());
        assertEquals(30000, voiture.getKilometrage());
        assertEquals("Très bon état", voiture.getDescription());
    }

    @Test
    void testPhotoEtContentType() {
        Voiture voiture = new Voiture();
        byte[] photo = {1, 2, 3, 4, 5};
        voiture.setPhoto(photo);
        voiture.setPhotoContentType("image/jpeg");
        
        assertArrayEquals(photo, voiture.getPhoto());
        assertEquals("image/jpeg", voiture.getPhotoContentType());
    }

    @Test
    void testRelationsUtilisateurEtPays() {
        Voiture voiture = new Voiture();
        Utilisateur utilisateur = new Utilisateur();
        Pays pays = new Pays();
        
        voiture.setUtilisateur(utilisateur);
        
        pays.setNom(PaysEnum.FRANCE);
        voiture.setPays(pays);
        
        assertSame(utilisateur, voiture.getUtilisateur());
        assertSame(pays, voiture.getPays());
        assertEquals(PaysEnum.FRANCE, voiture.getPays().getNom());
    }

    @Test
    void testToString() {
        Voiture voiture = new Voiture("Renault", "Clio", 15000.0, "Essence", 20000, "Comme neuve");
        Pays pays = new Pays(PaysEnum.FRANCE);
        voiture.setPays(pays);
        
        String expectedString = "Voiture{id=null, marque='Renault', modele='Clio', prix=15000.0, carburant='Essence', kilometrage=20000, photoContentType='null', description='Comme neuve', pays=FRANCE}";
        assertEquals(expectedString, voiture.toString());
    }
}