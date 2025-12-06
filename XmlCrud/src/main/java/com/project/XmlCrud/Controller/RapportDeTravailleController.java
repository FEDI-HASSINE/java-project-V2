package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.RapportRequest;
import com.project.XmlCrud.Model.Agent;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Model.RapportDeTravaille;
import com.project.XmlCrud.Service.AgentService;
import com.project.XmlCrud.Service.InterventionService;
import com.project.XmlCrud.Service.RapportDeTravailleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;

@RestController
public class RapportDeTravailleController {

    private static final String ROLE_AGENT = "agent";
    private static final String ROLE_CHEF = "chef";

    private final RapportDeTravailleService rapportService;
    private final InterventionService interventionService;
    private final AgentService agentService;

    public RapportDeTravailleController(RapportDeTravailleService rapportService,
                                        InterventionService interventionService,
                                        AgentService agentService) {
        this.rapportService = rapportService;
        this.interventionService = interventionService;
        this.agentService = agentService;
    }

    @PostMapping("/interventions/{interventionId}/rapports")
    public ResponseEntity<RapportDeTravaille> createRapport(@PathVariable Integer interventionId,
                                                            @Valid @RequestBody RapportRequest request,
                                                            Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_AGENT)) {
            throw new AccessDeniedException("Seul un agent peut créer un rapport de travail");
        }

        Agent agent = agentService.getAgentByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Agent introuvable pour l'utilisateur connecté"));

        Intervention intervention = interventionService.getInterventionById(interventionId);
        if (intervention == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Intervention introuvable");
        }

        if (!agent.getCin().equals(intervention.getCinAgent())) {
            throw new AccessDeniedException("Cet agent n'est pas affecté à l'intervention");
        }

        if (intervention.getEtat() == null || intervention.getEtat() < 100) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'intervention doit être terminée (etat = 100) pour créer un rapport");
        }

        if (rapportService.getRapportByInterventionRef(interventionId) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un rapport existe déjà pour cette intervention");
        }

        RapportDeTravaille rapport = new RapportDeTravaille(
                agent.getCin(),
                request.cout(),
                request.duree(),
                request.description(),
                decodeBase64OrEmpty(request.imageBase64()),
                interventionId
        );

        rapportService.addRapport(rapport);
        return ResponseEntity.status(HttpStatus.CREATED).body(rapport);
    }

    @GetMapping("/rapports")
    public List<RapportDeTravaille> getAllRapports(Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_CHEF)) {
            throw new AccessDeniedException("Seul le chef général peut consulter les rapports");
        }
        return rapportService.getAllRapports();
    }

    private static boolean hasAuthority(Authentication authentication, String authority) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> authority.equalsIgnoreCase(auth));
    }

    private static byte[] decodeBase64OrEmpty(String base64) {
        if (base64 == null || base64.isBlank()) {
            return new byte[0];
        }
        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image fournie n'est pas au format Base64 valide", ex);
        }
    }
}
