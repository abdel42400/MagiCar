package com.doranco.magicar.test;



import org.junit.jupiter.api.Test;

import com.doranco.magicar.model.Utilisateur;
import com.doranco.magicar.model.Voiture;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void testConstructeur() {
        Utilisateur utilisateur = new Utilisateur("Doe", "John", "0123456789", "john@example.com", "123 Rue Example", "motdepasse", "USER");
        
        assertEquals("Doe", utilisateur.getNom());
        assertEquals("John", utilisateur.getPrenom());
        assertEquals("0123456789", utilisateur.getNumero());
        assertEquals("john@example.com", utilisateur.getMail());
        assertEquals("123 Rue Example", utilisateur.getAdresse());
        assertEquals("motdepasse", utilisateur.getMotDePasse());
        assertEquals("USER", utilisateur.getRole());
    }

    @Test
    void testSettersGetters() {
        Utilisateur utilisateur = new Utilisateur();
        
        utilisateur.setNom("Smith");
        assertEquals("Smith", utilisateur.getNom());
        
        utilisateur.setPrenom("Jane");
        assertEquals("Jane", utilisateur.getPrenom());
        
        
    }

    @Test
    void testAjoutVoiture() {
        Utilisateur utilisateur = new Utilisateur();
        Voiture voiture = new Voiture(); 
        
        utilisateur.getVoitures().add(voiture);
        
        assertEquals(1, utilisateur.getVoitures().size());
        assertTrue(utilisateur.getVoitures().contains(voiture));
    }

    @Test
    void testToString() {
        Utilisateur utilisateur = new Utilisateur("Doe", "John", "0123456789", "john@example.com", "123 Rue Example", "motdepasse", "USER");
        
        String expectedString = "Utilisateur{id=null, nom='Doe', prenom='John', numero='0123456789', mail='john@example.com', adresse='123 Rue Example', role='USER', nombreDeVoitures=0}";
        assertEquals(expectedString, utilisateur.toString());
    }
}
