package com.project.XmlCrud.Service;

import com.project.XmlCrud.DTO.EquipementRequest;
import com.project.XmlCrud.Model.Equipement;
import com.project.XmlCrud.Model.EquipementIntervention;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EquipementService {

    private final ResponsableMunicipaliteService responsableMunicipaliteService;

    public EquipementService(ResponsableMunicipaliteService responsableMunicipaliteService) {
        this.responsableMunicipaliteService = responsableMunicipaliteService;
    }

    public Equipement addEquipement(EquipementRequest request, String responsableMunicipaliteEmail) {
        responsableMunicipaliteService.getResponsableMunicipaliteByEmail(normalizeEmail(responsableMunicipaliteEmail))
            .orElseThrow(() -> new IllegalArgumentException("ResponsableMunicipalite introuvable pour l'utilisateur connecté"));

        Municipalite municipalite = XmlUtil.loadMunicipalite();

        int nextId = municipalite.getEquipements().stream()
                .map(Equipement::getId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Equipement equipement = new Equipement();
        equipement.setId(nextId);
        equipement.setNom(request.getNom().trim());
        equipement.setDisponible(request.getDisponible() == null || request.getDisponible());

        municipalite.addEquipement(equipement);
        XmlUtil.saveMunicipalite(municipalite);

        return equipement;
    }

    public List<Equipement> getAllEquipements() {
        return XmlUtil.loadMunicipalite().getEquipements();
    }

    public List<Equipement> getAvailableEquipements() {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        
        // Get IDs of all equipments currently assigned to any intervention
        Set<Integer> assignedEquipementIds = municipalite.getEquipementInterventions().stream()
                .map(EquipementIntervention::getEquipementRef)
                .collect(Collectors.toSet());

        // Return only equipments that are NOT in the assigned set AND are marked available
        return municipalite.getEquipements().stream()
                .filter(e -> !assignedEquipementIds.contains(e.getId()) && e.isDisponible())
                .collect(Collectors.toList());
    }

    public void deleteEquipement(Integer id) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        Equipement equipement = municipalite.getEquipements().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Equipement introuvable"));

        // Check if equipment is used in any intervention
        boolean isUsed = municipalite.getEquipementInterventions().stream()
                .anyMatch(link -> link.getEquipementRef() == id);
        
        if (isUsed) {
             throw new IllegalStateException("Impossible de supprimer un équipement assigné à une intervention");
        }

        municipalite.getEquipements().remove(equipement);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public List<Equipement> getEquipementsForIntervention(Integer interventionId) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        ensureInterventionExists(municipalite, interventionId);

        Set<Integer> equipementIds = municipalite.getEquipementInterventions().stream()
                .filter(link -> interventionId.equals(link.getInterventionRef()))
                .map(EquipementIntervention::getEquipementRef)
                .collect(Collectors.toSet());

        return municipalite.getEquipements().stream()
                .filter(eq -> equipementIds.contains(eq.getId()))
                .toList();
    }

    public List<Equipement> assignEquipementsToIntervention(Integer interventionId,
                                                            List<Integer> equipementIds,
                                                            String responsableMunicipaliteEmail) {
        if (equipementIds == null || equipementIds.isEmpty()) {
            throw new IllegalArgumentException("Liste d'equipements obligatoire");
        }

        responsableMunicipaliteService.getResponsableMunicipaliteByEmail(normalizeEmail(responsableMunicipaliteEmail))
            .orElseThrow(() -> new IllegalArgumentException("ResponsableMunicipalite introuvable pour l'utilisateur connecté"));

        Municipalite municipalite = XmlUtil.loadMunicipalite();

        Intervention intervention = municipalite.getInterventions().stream()
                .filter(i -> interventionId.equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Intervention introuvable"));

        Map<Integer, Equipement> equipementMap = municipalite.getEquipements().stream()
                .collect(Collectors.toMap(Equipement::getId, eq -> eq));

        Set<Integer> uniqueIds = new LinkedHashSet<>(equipementIds);
        List<Equipement> assigned = new ArrayList<>();

        for (Integer equipementId : uniqueIds) {
            Equipement equipement = equipementMap.get(equipementId);
            if (equipement == null) {
                throw new NoSuchElementException("Equipement introuvable");
            }

            boolean alreadyLinked = municipalite.getEquipementInterventions().stream()
                    .anyMatch(link -> equipementId.equals(link.getEquipementRef()));
            if (alreadyLinked || !equipement.isDisponible()) {
                throw new IllegalStateException("Equipement " + equipementId + " indisponible");
            }

            municipalite.addEquipementIntervention(new EquipementIntervention(equipementId, intervention.getId()));
            equipement.setDisponible(false);
            assigned.add(equipement);
        }

        XmlUtil.saveMunicipalite(municipalite);
        return assigned;
    }

    private static void ensureInterventionExists(Municipalite municipalite, Integer interventionId) {
        boolean exists = municipalite.getInterventions().stream()
                .anyMatch(i -> interventionId.equals(i.getId()));
        if (!exists) {
            throw new NoSuchElementException("Intervention introuvable");
        }
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
