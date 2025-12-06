package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.SecretaireRequest;
import com.project.XmlCrud.DTO.SecretaireResponse;
import com.project.XmlCrud.Model.Secretaire;
import com.project.XmlCrud.Service.SecretaireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/secretaires")
public class SecretaireController {

    private static final String ROLE_SECRETAIRE = "secretaire";

    private final SecretaireService secretaireService;

    public SecretaireController(SecretaireService secretaireService) {
        this.secretaireService = secretaireService;
    }

    @GetMapping
    public List<SecretaireResponse> getAllSecretaires() {
        return secretaireService.getAllSecretaires()
                .stream()
                .map(SecretaireController::toResponse)
                .toList();
    }

    @GetMapping("/{cin}")
    public SecretaireResponse getSecretaire(@PathVariable String cin) {
        Secretaire secretaire = secretaireService.getSecretaireByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("Secretaire introuvable"));
        return toResponse(secretaire);
    }

    @PostMapping
    public ResponseEntity<SecretaireResponse> createSecretaire(@Valid @RequestBody SecretaireRequest request) {
        Secretaire secretaire = buildSecretaire(request);
        Secretaire created = secretaireService.addSecretaire(secretaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{cin}")
    public SecretaireResponse updateSecretaire(@PathVariable String cin, @Valid @RequestBody SecretaireRequest request) {
        Secretaire secretaire = buildSecretaire(request);
        secretaire.setCin(cin);
        boolean updated = secretaireService.updateSecretaire(secretaire);
        if (!updated) {
            throw new NoSuchElementException("Secretaire introuvable");
        }
        return toResponse(secretaire);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteSecretaire(@PathVariable String cin) {
        boolean removed = secretaireService.deleteSecretaire(cin);
        if (!removed) {
            throw new NoSuchElementException("Secretaire introuvable");
        }
        return ResponseEntity.noContent().build();
    }

    private static Secretaire buildSecretaire(SecretaireRequest request) {
        return new Secretaire(
                request.getCin().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_SECRETAIRE
        );
    }

    private static SecretaireResponse toResponse(Secretaire secretaire) {
        return new SecretaireResponse(
                secretaire.getCin(),
                secretaire.getEmail(),
                secretaire.getNom(),
                secretaire.getPrenom(),
                secretaire.getRole()
        );
    }
}
