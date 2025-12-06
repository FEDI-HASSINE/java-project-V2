package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.InterventionFromDemandeRequest;
import com.project.XmlCrud.DTO.UpdateInterventionEtatRequest;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Service.InterventionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterventionController {

    private static final String ROLE_SECRETAIRE = "secretaire";
    private static final String ROLE_AGENT = "agent";

    private final InterventionService interventionService;

    public InterventionController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @PostMapping("/demandes/{demandeId}/interventions")
    public ResponseEntity<Intervention> createFromDemande(@PathVariable Integer demandeId,
                                                          @Valid @RequestBody InterventionFromDemandeRequest request,
                                                          Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_SECRETAIRE)) {
            throw new AccessDeniedException("Seule une secretaire peut créer une intervention à partir d'une demande");
        }

        Intervention intervention = interventionService.createInterventionFromDemande(
                demandeId,
                request,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(intervention);
    }

    @PatchMapping("/interventions/{interventionId}/etat")
    public ResponseEntity<Intervention> updateEtat(@PathVariable Integer interventionId,
                                                   @Valid @RequestBody UpdateInterventionEtatRequest request,
                                                   Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_AGENT)) {
            throw new AccessDeniedException("Seul un agent peut mettre à jour l'état d'une intervention");
        }

        Intervention updated = interventionService.updateEtatByAgent(
                interventionId,
                request.getEtat(),
                authentication.getName()
        );

        return ResponseEntity.ok(updated);
    }

    private static boolean hasAuthority(Authentication authentication, String authority) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> authority.equalsIgnoreCase(auth));
    }
}
