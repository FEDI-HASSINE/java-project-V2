package com.project.XmlCrud.Service;

import com.project.XmlCrud.DTO.InterventionFromDemandeRequest;
import com.project.XmlCrud.DTO.UpdateInterventionRequest;
import com.project.XmlCrud.Model.Agent;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.Equipement;
import com.project.XmlCrud.Model.EquipementIntervention;
import com.project.XmlCrud.Model.Notification;
import com.project.XmlCrud.Model.ResponsableMunicipalite;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;

@Service
public class InterventionService {

    private final ResponsableMunicipaliteService responsableMunicipaliteService;
    private final AgentService agentService;

    public InterventionService(ResponsableMunicipaliteService responsableMunicipaliteService, AgentService agentService) {
        this.responsableMunicipaliteService = responsableMunicipaliteService;
        this.agentService = agentService;
    }

    public void addIntervention(Intervention intervention) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addIntervention(intervention);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public Intervention createInterventionFromDemande(Integer demandeId, InterventionFromDemandeRequest request, String responsableMunicipaliteEmail) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        ResponsableMunicipalite responsableMunicipalite = responsableMunicipaliteService.getResponsableMunicipaliteByEmail(normalizeEmail(responsableMunicipaliteEmail))
                .orElseThrow(() -> new IllegalArgumentException("ResponsableMunicipalite introuvable pour l'utilisateur connecté"));

        Demande demande = municipalite.getDemandes().stream()
                .filter(d -> demandeId.equals(d.getIdentifiant()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Demande introuvable"));

        Agent agent = agentService.getAgentByCIN(request.getAgentCin().trim())
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable pour le CIN fourni"));

        int nextId = municipalite.getInterventions().stream()
                .map(Intervention::getId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Intervention intervention = new Intervention();
        intervention.setId(nextId);
        intervention.setBudget(request.getBudget());
        intervention.setDateDebut(request.getDateDebut());
        intervention.setType(request.getType().trim());
        intervention.setUrgence(request.getUrgence());
        intervention.setEtat(request.getEtat() == null ? 0 : request.getEtat());

        String localisation = request.getLocalisation();
        if (localisation == null || localisation.isBlank()) {
            localisation = demande.getLocalisation();
        } else {
            localisation = localisation.trim();
        }
        intervention.setLocalisation(localisation);

        intervention.setImage(resolveImageBytes(request.getImageBase64(), demande.getImage()));
        intervention.setCinAgent(agent.getCin());
        intervention.setCinResponsableMunicipalite(responsableMunicipalite.getCin());

        // 1. Add Intervention
        municipalite.addIntervention(intervention);

        // 1bis. Marquer l'agent comme indisponible après affectation
        for (int i = 0; i < municipalite.getAgents().size(); i++) {
            Agent a = municipalite.getAgents().get(i);
            if (a.getCin().equals(agent.getCin())) {
                a.setDisponibilite(false);
                municipalite.getAgents().set(i, a);
                break;
            }
        }

        // 2. Remove Demande
        municipalite.getDemandes().remove(demande);

        // 3. Create Notification
        int nextNotifId = municipalite.getNotifications().stream()
                .map(Notification::getIdN)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
        
        Notification notification = new Notification(
                nextNotifId,
                demande.getCitoyenRef(),
                "Demande numero " + demande.getIdentifiant() + " est accepte"
        );
        municipalite.addNotification(notification);

        XmlUtil.saveMunicipalite(municipalite);
        return intervention;
    }

    public List<Intervention> getInterventionsByAgent(String cin) {
        return XmlUtil.loadMunicipalite().getInterventions()
                .stream()
                .filter(i -> cin.equals(i.getCinAgent()))
                .toList();
    }

    public List<Intervention> getAllInterventions() {
        return XmlUtil.loadMunicipalite().getInterventions();
    }

    public Intervention getInterventionById(Integer id) {
        return XmlUtil.loadMunicipalite().getInterventions()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean updateIntervention(Intervention updatedIntervention) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getInterventions().size(); i++) {
            Intervention existing = municipalite.getInterventions().get(i);
            if (existing.getId().equals(updatedIntervention.getId())) {
                municipalite.getInterventions().set(i, updatedIntervention);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteIntervention(Integer id) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.removeInterventionById(id);
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }

    public Intervention updateEtatByAgent(Integer interventionId, Integer etat, String agentEmail) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        Agent agent = agentService.getAgentByEmail(normalizeEmail(agentEmail))
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable pour l'utilisateur connecté"));

        Intervention intervention = municipalite.getInterventions().stream()
                .filter(i -> interventionId.equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Intervention introuvable"));

        if (!agent.getCin().equals(intervention.getCinAgent())) {
            throw new AccessDeniedException("Cet agent n'est pas affecté à l'intervention");
        }

        intervention.setEtat(etat == null ? 0 : etat);
        XmlUtil.saveMunicipalite(municipalite);
        return intervention;
    }

    public Intervention updateInterventionByResponsableMunicipalite(Integer interventionId, UpdateInterventionRequest request, String responsableMunicipaliteEmail) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        // Vérifier responsableMunicipalite
        responsableMunicipaliteService.getResponsableMunicipaliteByEmail(normalizeEmail(responsableMunicipaliteEmail))
                .orElseThrow(() -> new IllegalArgumentException("ResponsableMunicipalite introuvable pour l'utilisateur connecté"));

        // Intervention existante
        Intervention intervention = municipalite.getInterventions().stream()
                .filter(i -> interventionId.equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Intervention introuvable"));

        // Mise à jour des champs simples
        intervention.setBudget(request.getBudget());
        intervention.setDateDebut(request.getDateDebut());
        intervention.setType(request.getType().trim());
        intervention.setUrgence(request.getUrgence());
        // La responsableMunicipalite ne peut pas modifier l'état; on conserve la valeur existante
        // intervention.setEtat(...) volontairement ignoré pour ce rôle
        if (request.getLocalisation() != null) {
            intervention.setLocalisation(request.getLocalisation().trim());
        }

        // Gestion du changement d'agent: rendre l'ancien disponible, le nouveau indisponible
        String oldCin = intervention.getCinAgent();
        String newCin = request.getAgentCin().trim();
        if (!newCin.equals(oldCin)) {
            // Ancien agent -> disponible
            municipalite.getAgents().stream()
                    .filter(a -> a.getCin().equals(oldCin))
                    .findFirst().ifPresent(a -> a.setDisponibilite(true));
            // Nouveau agent -> vérifier existence
            Agent newAgent = municipalite.getAgents().stream()
                    .filter(a -> a.getCin().equals(newCin))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Nouvel agent introuvable"));
            // Nouveau agent -> indisponible
            newAgent.setDisponibilite(false);
            intervention.setCinAgent(newCin);
        }

        // Remplacement des équipements liés si fourni
        if (request.getEquipementIds() != null) {
            // Construire set pour éviter doublons
            Set<Integer> desired = new LinkedHashSet<>(request.getEquipementIds());

            // Équipements actuellement liés à cette intervention
            Set<Integer> current = municipalite.getEquipementInterventions().stream()
                    .filter(link -> interventionId.equals(link.getInterventionRef()))
                    .map(EquipementIntervention::getEquipementRef)
                    .collect(java.util.stream.Collectors.toSet());

            // Équipements à retirer
            Set<Integer> toRemove = new java.util.HashSet<>(current);
            toRemove.removeAll(desired);

            // Équipements à ajouter
            Set<Integer> toAdd = new java.util.HashSet<>(desired);
            toAdd.removeAll(current);

            // Retirer les liens et rendre disponibles
            if (!toRemove.isEmpty()) {
                municipalite.getEquipementInterventions().removeIf(link ->
                        interventionId.equals(link.getInterventionRef()) && toRemove.contains(link.getEquipementRef()));
                municipalite.getEquipements().forEach(eq -> {
                    if (toRemove.contains(eq.getId())) {
                        eq.setDisponible(true);
                    }
                });
            }

            // Ajouter les nouveaux liens en vérifiant disponibilité
            if (!toAdd.isEmpty()) {
                for (Integer eqId : toAdd) {
                    Equipement eq = municipalite.getEquipements().stream()
                            .filter(e -> e.getId().equals(eqId))
                            .findFirst()
                            .orElseThrow(() -> new NoSuchElementException("Equipement introuvable: " + eqId));
                    boolean alreadyLinkedElsewhere = municipalite.getEquipementInterventions().stream()
                            .anyMatch(link -> link.getEquipementRef().equals(eqId));
                    if (alreadyLinkedElsewhere || !eq.isDisponible()) {
                        throw new IllegalStateException("Equipement " + eqId + " indisponible");
                    }
                    municipalite.addEquipementIntervention(new EquipementIntervention(eqId, interventionId));
                    eq.setDisponible(false);
                }
            }
        }

        XmlUtil.saveMunicipalite(municipalite);
        return intervention;
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static byte[] resolveImageBytes(String base64, byte[] fallback) {
        if (base64 == null || base64.isBlank()) {
            return fallback == null ? new byte[0] : fallback;
        }
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Image fournie n'est pas au format Base64 valide", ex);
        }
    }
}
