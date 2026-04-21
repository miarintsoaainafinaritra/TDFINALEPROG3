package com.federation.tdfinale.service;

import com.federation.tdfinale.model.*;
import com.federation.tdfinale.repository.FederationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FederationService {
    private final FederationRepository repo;
    public FederationService(FederationRepository repo) { this.repo = repo; }

    public Collectivite creerCollectivite(String n, String v, String s, List<Long> ids, boolean aut) {
        if (!aut || ids == null || ids.size() < 10) throw new RuntimeException("Conditions non remplies");
        long anciens = ids.stream().distinct().map(repo::findMembreById).filter(Optional::isPresent).map(Optional::get)
                .filter(m -> m.getCollectiviteId() == null && m.getDateAdhesion() != null && !m.getDateAdhesion().isAfter(LocalDate.now().minusMonths(6))).count();
        if (anciens < 10) throw new RuntimeException("10 membres libres avec 6 mois d'ancienneté requis");
        Collectivite c = repo.saveCollectivite(new Collectivite(n, v, s));
        ids.stream().distinct().forEach(id -> repo.findMembreById(id).ifPresent(m -> {
            m.setCollectiviteId(c.getId()); repo.updateMembre(m); c.getMembres().add(m);
        }));
        return c;
    }

    public Membre admettreMembre(Long cid, Long pid, String n, String p, String e, String t, String mp) {
        Collectivite c = repo.findCollectiviteById(cid).orElseThrow(() -> new RuntimeException("Col introuvable"));
        Membre par = repo.findMembreById(pid).orElseThrow(() -> new RuntimeException("Parrain introuvable"));
        if (!"membre_confirme".equals(par.getPoste()) || (par.getDateAdhesion() != null && par.getDateAdhesion().isAfter(LocalDate.now().minusDays(90))))
            throw new RuntimeException("Parrain non valide");
        Membre m = repo.saveMembre(new Membre(n, p, e, t));
        m.setParrainId(pid); m.setCollectiviteId(cid); c.getMembres().add(m);
        enregistrerCotisation(cid, m.getId(), 50000.0, mp, "DROIT_ADHESION");
        return m;
    }

    public void changerCollectivite(Long mid, Long ncid) {
        Membre m = repo.findMembreById(mid).orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        Collectivite n = repo.findCollectiviteById(ncid).orElseThrow(() -> new RuntimeException("Col non trouvée"));
        if (m.getCollectiviteId() != null) repo.findCollectiviteById(m.getCollectiviteId()).ifPresent(c -> c.getMembres().removeIf(x -> x.getId().equals(mid)));
        m.setCollectiviteId(ncid); repo.updateMembre(m); n.getMembres().add(m);
    }

    public void demissionner(Long mid) {
        Membre m = repo.findMembreById(mid).orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        if (m.getCollectiviteId() != null) repo.findCollectiviteById(m.getCollectiviteId()).ifPresent(c -> c.getMembres().removeIf(x -> x.getId().equals(mid)));
        m.setCollectiviteId(null); repo.updateMembre(m);
    }

    public Cotisation enregistrerCotisation(Long cid, Long mid, Double mon, String mp, String t) {
        Collectivite c = repo.findCollectiviteById(cid).orElseThrow(() -> new RuntimeException("Col non trouvée"));
        repo.findMembreById(mid).orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        Compte caisse = c.getComptes().stream().filter(cp -> "caisse".equals(cp.getType())).findFirst().orElseThrow(() -> new RuntimeException("Caisse non trouvée"));
        caisse.crediter(mon);
        return repo.saveCotisation(new Cotisation(mid, cid, mon, mp, t));
    }

    public List<Cotisation> listerCotisations(Long cid) {
        return repo.findCotisationsByCollectiviteId(cid);
    }

    public Compte ajouterCompte(Long cid, String t, String tit) {
        Collectivite c = repo.findCollectiviteById(cid).orElseThrow(() -> new RuntimeException("Col non trouvée"));
        if ("caisse".equals(t) && c.getComptes().stream().anyMatch(cp -> "caisse".equals(cp.getType()))) throw new RuntimeException("Une seule caisse autorisée");
        Compte cp = repo.saveCompte(new Compte(t, tit)); c.getComptes().add(cp); return cp;
    }

    public Double consulterSolde(Long cpid) { return repo.findCompteById(cpid).orElseThrow(() -> new RuntimeException("Compte non trouvé")).getSolde(); }

    public Activite creerActiviteExceptionnelle(Long cid, String n, LocalDate d, boolean o) {
        repo.findCollectiviteById(cid).orElseThrow(() -> new RuntimeException("Col non trouvée"));
        Activite a = new Activite(n, d, o); a.setCollectiviteId(cid); return repo.saveActivite(a);
    }

    public void enregistrerPresence(Long aid, Long mid, boolean p, boolean e) {
        Activite a = repo.findActiviteById(aid).orElseThrow(() -> new RuntimeException("Activité non trouvée"));
        a.setPresence(mid, e || p);
    }

    public double tauxAssiduite(Long mid, LocalDate d, LocalDate f) {
        Membre m = repo.findMembreById(mid).orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        List<Activite> acts = repo.findAllActivites().stream().filter(a -> a.getCollectiviteId().equals(m.getCollectiviteId()) && !a.getDate().isBefore(d) && !a.getDate().isAfter(f)).toList();
        return acts.isEmpty() ? 0 : (double) acts.stream().filter(a -> a.getPresences().getOrDefault(mid, false)).count() / acts.size() * 100;
    }
}