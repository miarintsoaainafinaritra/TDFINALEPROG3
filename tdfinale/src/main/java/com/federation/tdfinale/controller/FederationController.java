package com.federation.tdfinale.controller;

import com.federation.tdfinale.model.*;
import com.federation.tdfinale.service.FederationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FederationController {
    private final FederationService service;
    public FederationController(FederationService service) { this.service = service; }

    @PostMapping("/collectivites")
    public ResponseEntity<Collectivite> creerCollectivite(@RequestParam String nom, @RequestParam String ville, @RequestParam String specialite, @RequestBody List<Long> membresIds, @RequestParam boolean autorisation) {
        return ResponseEntity.ok(service.creerCollectivite(nom, ville, specialite, membresIds, autorisation));
    }

    @PostMapping("/collectivites/{id}/membres")
    public ResponseEntity<Membre> admettreMembre(@PathVariable Long id, @RequestParam Long parrainId, @RequestParam String nom, @RequestParam String prenom, @RequestParam String email, @RequestParam String telephone, @RequestParam String modePaiement) {
        return ResponseEntity.ok(service.admettreMembre(id, parrainId, nom, prenom, email, telephone, modePaiement));
    }

    @PutMapping("/membres/{id}/transfert")
    public ResponseEntity<Void> transferer(@PathVariable Long id, @RequestParam Long nouvelleCollectiviteId) {
        service.changerCollectivite(id, nouvelleCollectiviteId); return ResponseEntity.ok().build();
    }

    @PostMapping("/membres/{id}/demission")
    public ResponseEntity<Void> demissionner(@PathVariable Long id) {
        service.demissionner(id); return ResponseEntity.noContent().build();
    }

    @PostMapping("/collectivites/{id}/cotisations")
    public ResponseEntity<Cotisation> cotiser(@PathVariable Long id, @RequestParam Long membreId, @RequestParam Double montant, @RequestParam String modePaiement, @RequestParam String type) {
        return ResponseEntity.ok(service.enregistrerCotisation(id, membreId, montant, modePaiement, type));
    }

    @GetMapping("/collectivites/{id}/cotisations")
    public ResponseEntity<List<Cotisation>> listerCotisations(@PathVariable Long id) {
        return ResponseEntity.ok(service.listerCotisations(id));
    }

    @PostMapping("/collectivites/{id}/comptes")
    public ResponseEntity<Compte> ajouterCompte(@PathVariable Long id, @RequestParam String type, @RequestParam String titulaire) {
        return ResponseEntity.ok(service.ajouterCompte(id, type, titulaire));
    }

    @GetMapping("/comptes/{id}/solde")
    public ResponseEntity<Double> getSolde(@PathVariable Long id) { return ResponseEntity.ok(service.consulterSolde(id)); }

    @PostMapping("/collectivites/{id}/activites")
    public ResponseEntity<Activite> creerActivite(@PathVariable Long id, @RequestParam String nom, @RequestParam String date, @RequestParam boolean obligatoire) {
        return ResponseEntity.ok(service.creerActiviteExceptionnelle(id, nom, LocalDate.parse(date), obligatoire));
    }

    @PostMapping("/activites/{id}/presences")
    public ResponseEntity<Void> presence(@PathVariable Long id, @RequestParam Long membreId, @RequestParam boolean present, @RequestParam(required = false, defaultValue = "false") boolean excuse) {
        service.enregistrerPresence(id, membreId, present, excuse); return ResponseEntity.ok().build();
    }

    @GetMapping("/membres/{id}/assiduite")
    public ResponseEntity<Double> assiduite(@PathVariable Long id, @RequestParam String debut, @RequestParam String fin) {
        return ResponseEntity.ok(service.tauxAssiduite(id, LocalDate.parse(debut), LocalDate.parse(fin)));
    }
}