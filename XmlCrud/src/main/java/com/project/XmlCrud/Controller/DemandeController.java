package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.DemandeDTO;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Service.DemandeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Demande> createDemande(@Valid @RequestBody DemandeDTO demandeDTO) {
        Demande created = demandeService.createDemande(demandeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Demande> getAllDemandes(Authentication authentication) {
        if (hasAuthority(authentication, "citoyen")) {
            return demandeService.getDemandesForCitizen(authentication.getName());
        }
        return demandeService.getAllDemandes();
    }

    @GetMapping("/{id}")
    public Demande getDemandeById(@PathVariable Integer id, Authentication authentication) {
        if (hasAuthority(authentication, "citoyen")) {
            return demandeService.getDemandeForCitizen(id, authentication.getName());
        }
        return demandeService.getDemandeById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Integer id) {
        demandeService.deleteDemande(id);
        return ResponseEntity.noContent().build();
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
