package com.project.XmlCrud.Service;

import com.project.XmlCrud.DTO.InterventionFromDemandeRequest;
import com.project.XmlCrud.Model.Agent;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.Secretaire;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InterventionService {

    private final SecretaireService secretaireService;
    private final AgentService agentService;

    public InterventionService(SecretaireService secretaireService, AgentService agentService) {
        this.secretaireService = secretaireService;
        this.agentService = agentService;
    }

    public void addIntervention(Intervention intervention) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addIntervention(intervention);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public Intervention createInterventionFromDemande(Integer demandeId, InterventionFromDemandeRequest request, String secretaireEmail) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        Secretaire secretaire = secretaireService.getSecretaireByEmail(normalizeEmail(secretaireEmail))
                .orElseThrow(() -> new IllegalArgumentException("Secretaire introuvable pour l'utilisateur connecté"));

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
        intervention.setCinSecretaire(secretaire.getCin());

        municipalite.addIntervention(intervention);
        XmlUtil.saveMunicipalite(municipalite);
        return intervention;
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
