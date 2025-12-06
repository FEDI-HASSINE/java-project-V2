package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.EquipementAssignmentRequest;
import com.project.XmlCrud.DTO.EquipementRequest;
import com.project.XmlCrud.Model.Equipement;
import com.project.XmlCrud.Service.EquipementService;
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

import java.util.List;

@RestController
public class EquipementController {

    private static final String ROLE_RESPONSABLE_MUNICIPALITE = "responsableMunicipalite";

    private final EquipementService equipementService;

    public EquipementController(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @PostMapping("/equipements")
    public ResponseEntity<Equipement> addEquipement(@Valid @RequestBody EquipementRequest request,
                                                    Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_RESPONSABLE_MUNICIPALITE)) {
            throw new AccessDeniedException("Seule une responsableMunicipalite peut ajouter un equipement");
        }

        Equipement created = equipementService.addEquipement(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/equipements")
    public List<Equipement> getEquipements(Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_RESPONSABLE_MUNICIPALITE) && !hasAuthority(authentication, "chef")) {
            throw new AccessDeniedException("Accès refusé");
        }
        return equipementService.getAllEquipements();
    }

    @GetMapping("/interventions/{interventionId}/equipements")
    public List<Equipement> getEquipementsForIntervention(@PathVariable Integer interventionId,
                                                           Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_RESPONSABLE_MUNICIPALITE)) {
            throw new AccessDeniedException("Seule une responsableMunicipalite peut consulter les equipements d'une intervention");
        }
        return equipementService.getEquipementsForIntervention(interventionId);
    }

    @PostMapping("/interventions/{interventionId}/equipements")
    public List<Equipement> assignEquipements(@PathVariable Integer interventionId,
                                               @Valid @RequestBody EquipementAssignmentRequest request,
                                               Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_RESPONSABLE_MUNICIPALITE)) {
            throw new AccessDeniedException("Seule une responsableMunicipalite peut affecter des equipements");
        }
        return equipementService.assignEquipementsToIntervention(
                interventionId,
                request.getEquipementIds(),
                authentication.getName()
        );
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
