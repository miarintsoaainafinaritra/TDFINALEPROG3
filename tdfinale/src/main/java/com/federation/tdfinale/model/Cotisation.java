package com.federation.tdfinale.model;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cotisation {
    private Long id, membreId, collectiviteId;
    private Double montant;
    private LocalDate date;
    private String modePaiement, type;

    public Cotisation(Long mid, Long cid, Double m, String mp, String t) {
        this.membreId = mid;
        this.collectiviteId = cid;
        this.montant = m;
        this.modePaiement = mp;
        this.type = t;
        this.date = LocalDate.now();
    }
}