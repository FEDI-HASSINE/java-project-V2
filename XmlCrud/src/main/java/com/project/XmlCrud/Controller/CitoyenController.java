package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.CitoyenRequest;
import com.project.XmlCrud.DTO.CitoyenResponse;
import com.project.XmlCrud.Model.Citoyen;
import com.project.XmlCrud.Service.CitoyenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/citoyens")
public class CitoyenController {

    private static final String ROLE_CITOYEN = "citoyen";

    private final CitoyenService citoyenService;

    public CitoyenController(CitoyenService citoyenService) {
        this.citoyenService = citoyenService;
    }

    @GetMapping
    public List<CitoyenResponse> getAllCitoyens() {
        return citoyenService.getAllCitoyens()
                .stream()
                .map(CitoyenController::toResponse)
                .toList();
    }

    @GetMapping("/{cin}")
    public CitoyenResponse getCitoyen(@PathVariable String cin) {
        Citoyen citoyen = citoyenService.getCitoyenByCIN(cin);
        if (citoyen == null) {
            throw new NoSuchElementException("Citoyen introuvable");
        }
        return toResponse(citoyen);
    }

    @PutMapping("/{cin}")
    public CitoyenResponse updateCitoyen(@PathVariable String cin,
                                          @Valid @RequestBody CitoyenRequest request) {
        Citoyen citoyen = buildCitoyen(request);
        citoyen.setCin(cin);

        boolean updated = citoyenService.updateCitoyen(citoyen);
        if (!updated) {
            throw new NoSuchElementException("Citoyen introuvable");
        }
        return toResponse(citoyen);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteCitoyen(@PathVariable String cin) {
        boolean removed = citoyenService.deleteCitoyen(cin);
        if (!removed) {
            throw new NoSuchElementException("Citoyen introuvable");
        }
        return ResponseEntity.noContent().build();
    }

    private static Citoyen buildCitoyen(CitoyenRequest request) {
        Citoyen citoyen = new Citoyen(
                request.getCin().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_CITOYEN,
                request.getAdresse().trim(),
                request.getRue().trim()
        );
        return citoyen;
    }

    private static CitoyenResponse toResponse(Citoyen citoyen) {
        return new CitoyenResponse(
                citoyen.getCin(),
                citoyen.getEmail(),
                citoyen.getNom(),
                citoyen.getPrenom(),
                citoyen.getAdresse(),
                citoyen.getRue(),
                citoyen.getRole()
        );
    }
}
