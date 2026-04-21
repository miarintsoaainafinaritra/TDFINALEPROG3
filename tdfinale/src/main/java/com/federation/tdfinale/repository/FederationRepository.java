package com.federation.tdfinale.repository;

import com.federation.tdfinale.model.*;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FederationRepository {
    private final Map<Long, Collectivite> collectivites = new ConcurrentHashMap<>();
    private final Map<Long, Membre> membres = new ConcurrentHashMap<>();
    private final Map<Long, Compte> comptes = new ConcurrentHashMap<>();
    private final Map<Long, Activite> activites = new ConcurrentHashMap<>();
    private final Map<Long, Cotisation> cotisations = new ConcurrentHashMap<>();

    private final AtomicLong cId = new AtomicLong(1), mId = new AtomicLong(1), cpId = new AtomicLong(1), aId = new AtomicLong(1), cotId = new AtomicLong(1);

    public Collectivite saveCollectivite(Collectivite c) { if (c.getId() == null) c.setId(cId.getAndIncrement()); collectivites.put(c.getId(), c); return c; }
    public Optional<Collectivite> findCollectiviteById(Long id) { return Optional.ofNullable(collectivites.get(id)); }

    public Membre saveMembre(Membre m) { if (m.getId() == null) m.setId(mId.getAndIncrement()); membres.put(m.getId(), m); return m; }
    public Optional<Membre> findMembreById(Long id) { return Optional.ofNullable(membres.get(id)); }
    public void updateMembre(Membre m) { membres.put(m.getId(), m); }

    public Compte saveCompte(Compte c) { if (c.getId() == null) c.setId(cpId.getAndIncrement()); comptes.put(c.getId(), c); return c; }
    public Optional<Compte> findCompteById(Long id) { return Optional.ofNullable(comptes.get(id)); }

    public Activite saveActivite(Activite a) { if (a.getId() == null) a.setId(aId.getAndIncrement()); activites.put(a.getId(), a); return a; }
    public Optional<Activite> findActiviteById(Long id) { return Optional.ofNullable(activites.get(id)); }
    public List<Activite> findAllActivites() { return new ArrayList<>(activites.values()); }

    public Cotisation saveCotisation(Cotisation c) { if (c.getId() == null) c.setId(cotId.getAndIncrement()); cotisations.put(c.getId(), c); return c; }
    public List<Cotisation> findCotisationsByCollectiviteId(Long cid) { return cotisations.values().stream().filter(c -> c.getCollectiviteId().equals(cid)).toList(); }
}