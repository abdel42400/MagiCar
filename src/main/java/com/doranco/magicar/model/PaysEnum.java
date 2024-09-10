package com.doranco.magicar.model;

public enum PaysEnum {
    ALLEMAGNE,
    AUTRICHE,
    BELGIQUE,
    BULGARIE,
    CHYPRE,
    CROATIE,
    DANEMARK,
    ESPAGNE,
    ESTONIE,
    FINLANDE,
    FRANCE,
    GRECE,
    HONGRIE,
    IRLANDE,
    ITALIE,
    LETTONIE,
    LITUANIE,
    LUXEMBOURG,
    MALTE,
    PAYS_BAS,
    POLOGNE,
    PORTUGAL,
    REPUBLIQUE_TCHEQUE,
    ROUMANIE,
    SLOVAQUIE,
    SLOVENIE,
    SUEDE;

    public String getNom() {
        return this.name().toLowerCase().replace("_", " ");
    }
}