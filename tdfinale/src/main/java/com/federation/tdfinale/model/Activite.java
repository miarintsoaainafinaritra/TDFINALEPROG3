package com.federation.tdfinale.model;

import lombok.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activite {
    private Long id;
    private String nom;
    private LocalDate date;
    private boolean obligatoire;
    private Long collectiviteId;
    private Map<Long, Boolean> presences = new HashMap<>();

    public Activite(String nom, LocalDate date, boolean obligatoire) {
        this.nom = nom;
        this.date = date;
        this.obligatoire = obligatoire;
    }

    public void setPresence(Long mid, boolean p) { presences.put(mid, p); }
}