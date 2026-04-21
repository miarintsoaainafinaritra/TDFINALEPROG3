package com.federation.tdfinale.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    private Long id;
    private String type, titulaire, banque, numeroCompte, serviceMobile, numeroTelephone;
    private Double solde = 0.0;

    public Compte(String type, String titulaire) {
        this.type = type;
        this.titulaire = titulaire;
    }

    public void crediter(Double m) { this.solde += m; }
}