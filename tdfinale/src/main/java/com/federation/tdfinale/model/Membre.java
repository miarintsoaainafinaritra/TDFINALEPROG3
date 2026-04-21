package com.federation.tdfinale.model;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membre {
    private Long id;
    private String nom, prenom, genre, adresse, metier, telephone, email, poste;
    private LocalDate dateNaissance, dateAdhesion;
    private Long collectiviteId, parrainId;

    public Membre(String nom, String prenom, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.dateAdhesion = LocalDate.now();
        this.poste = "membre_junior";
    }
}