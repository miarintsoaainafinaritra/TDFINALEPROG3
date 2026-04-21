package com.federation.tdfinale.model;

import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collectivite {
    private Long id;
    private String nom, ville, specialite;
    private LocalDate dateCreation;
    private List<Membre> membres = new ArrayList<>();
    private List<Compte> comptes = new ArrayList<>();
    private List<Activite> activites = new ArrayList<>();

    public Collectivite(String nom, String ville, String specialite) {
        this.nom = nom;
        this.ville = ville;
        this.specialite = specialite;
        this.dateCreation = LocalDate.now();
    }
}