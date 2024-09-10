package com.doranco.magicar.test;



import org.junit.jupiter.api.Test;

import com.doranco.magicar.model.Pays;
import com.doranco.magicar.model.PaysEnum;
import com.doranco.magicar.model.Voiture;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class PaysTest {

    @Test
    void testConstructeur() {
        Pays pays = new Pays(PaysEnum.FRANCE);
        assertEquals(PaysEnum.FRANCE, pays.getNom());
    }

    @Test
    void testSettersEtGetters() {
        Pays pays = new Pays();
        
        pays.setId(1L);
        pays.setNom(PaysEnum.ALLEMAGNE);
        
        assertEquals(1L, pays.getId());
        assertEquals(PaysEnum.ALLEMAGNE, pays.getNom());
    }

    @Test
    void testAjoutVoiture() {
        Pays pays = new Pays(PaysEnum.ITALIE);
        Voiture voiture = new Voiture();
        
        pays.getVoitures().add(voiture);
        
        assertEquals(1, pays.getVoitures().size());
        assertTrue(pays.getVoitures().contains(voiture));
    }

    @Test
    void testToString() {
        Pays pays = new Pays(PaysEnum.ESPAGNE);
        pays.setId(2L);
        
        Voiture voiture1 = new Voiture();
        Voiture voiture2 = new Voiture();
        pays.getVoitures().add(voiture1);
        pays.getVoitures().add(voiture2);
        
        String expectedString = "Pays{id=2, nom='espagne', nombreDeVoitures=2}";
        assertEquals(expectedString, pays.toString());
    }

    @Test
    void testSetVoitures() {
        Pays pays = new Pays(PaysEnum.BELGIQUE);
        List<Voiture> voitures = new ArrayList<>();
        voitures.add(new Voiture());
        voitures.add(new Voiture());
        voitures.add(new Voiture());
        
        pays.setVoitures(voitures);
        
        assertEquals(3, pays.getVoitures().size());
    }
}