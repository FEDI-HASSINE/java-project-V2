package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.ResponsableMunicipaliteRequest;
import com.project.XmlCrud.DTO.ResponsableMunicipaliteResponse;
import com.project.XmlCrud.Model.ResponsableMunicipalite;
import com.project.XmlCrud.Service.ResponsableMunicipaliteService;
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
@RequestMapping("/responsableMunicipalites")
public class ResponsableMunicipaliteController {

    private static final String ROLE_RESPONSABLE_MUNICIPALITE = "responsable_municipalite";

    private final ResponsableMunicipaliteService responsableMunicipaliteService;

    public ResponsableMunicipaliteController(ResponsableMunicipaliteService responsableMunicipaliteService) {
        this.responsableMunicipaliteService = responsableMunicipaliteService;
    }

    @GetMapping
    public List<ResponsableMunicipaliteResponse> getAllResponsableMunicipalites() {
        return responsableMunicipaliteService.getAllResponsableMunicipalites()
                .stream()
                .map(ResponsableMunicipaliteController::toResponse)
                .toList();
    }

    @GetMapping("/{cin}")
    public ResponsableMunicipaliteResponse getResponsableMunicipalite(@PathVariable String cin) {
        ResponsableMunicipalite responsableMunicipalite = responsableMunicipaliteService.getResponsableMunicipaliteByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("ResponsableMunicipalite introuvable"));
        return toResponse(responsableMunicipalite);
    }

    @PostMapping
    public ResponseEntity<?> createResponsableMunicipalite(@Valid @RequestBody ResponsableMunicipaliteRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Le mot de passe doit contenir au moins 6 caractères");
        }
        ResponsableMunicipalite responsableMunicipalite = buildResponsableMunicipalite(request);
        ResponsableMunicipalite created = responsableMunicipaliteService.addResponsableMunicipalite(responsableMunicipalite);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{cin}")
    public ResponsableMunicipaliteResponse updateResponsableMunicipalite(@PathVariable String cin, @Valid @RequestBody ResponsableMunicipaliteRequest request) {
        ResponsableMunicipalite existing = responsableMunicipaliteService.getResponsableMunicipaliteByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("ResponsableMunicipalite introuvable"));

        String passwordToUse = request.getPassword();
        if (passwordToUse == null || passwordToUse.isEmpty()) {
            passwordToUse = existing.getPassword();
        } else {
            if (passwordToUse.length() < 6) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
            }
        }

        ResponsableMunicipalite responsableMunicipalite = new ResponsableMunicipalite(
                cin,
                request.getEmail().trim().toLowerCase(),
                passwordToUse,
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_RESPONSABLE_MUNICIPALITE
        );

        boolean updated = responsableMunicipaliteService.updateResponsableMunicipalite(responsableMunicipalite);
        if (!updated) {
            throw new NoSuchElementException("ResponsableMunicipalite introuvable");
        }
        return toResponse(responsableMunicipalite);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteResponsableMunicipalite(@PathVariable String cin) {
        boolean removed = responsableMunicipaliteService.deleteResponsableMunicipalite(cin);
        if (!removed) {
            throw new NoSuchElementException("ResponsableMunicipalite introuvable");
        }
        return ResponseEntity.noContent().build();
    }

    private static ResponsableMunicipalite buildResponsableMunicipalite(ResponsableMunicipaliteRequest request) {
        return new ResponsableMunicipalite(
                request.getCin().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_RESPONSABLE_MUNICIPALITE
        );
    }

    private static ResponsableMunicipaliteResponse toResponse(ResponsableMunicipalite responsableMunicipalite) {
        return new ResponsableMunicipaliteResponse(
                responsableMunicipalite.getCin(),
                responsableMunicipalite.getEmail(),
                responsableMunicipalite.getNom(),
                responsableMunicipalite.getPrenom(),
                responsableMunicipalite.getRole()
        );
    }
}
