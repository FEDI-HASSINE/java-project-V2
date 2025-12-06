package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.ChefInformatiqueRequest;
import com.project.XmlCrud.DTO.ChefInformatiqueResponse;
import com.project.XmlCrud.Model.ChefInformatique;
import com.project.XmlCrud.Service.ChefInformatiqueService;
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
@RequestMapping("/chefs-informatiques")
public class ChefInformatiqueController {

    private static final String ROLE_CHEF_INFO = "chefinfo";

    private final ChefInformatiqueService chefInformatiqueService;

    public ChefInformatiqueController(ChefInformatiqueService chefInformatiqueService) {
        this.chefInformatiqueService = chefInformatiqueService;
    }

    @PostMapping
    public ResponseEntity<ChefInformatiqueResponse> createChefInformatique(@Valid @RequestBody ChefInformatiqueRequest request) {
        ChefInformatique chef = buildChef(request);
        chefInformatiqueService.addChefInformatique(chef);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(chef));
    }

    @GetMapping
    public List<ChefInformatiqueResponse> getAllChefsInformatiques() {
        return chefInformatiqueService.getAllChefs()
                .stream()
                .map(ChefInformatiqueController::toResponse)
                .toList();
    }

    @GetMapping("/{cin}")
    public ChefInformatiqueResponse getChefInformatique(@PathVariable String cin) {
        ChefInformatique chef = chefInformatiqueService.getChefByCIN(cin);
        if (chef == null) {
            throw new NoSuchElementException("Chef informatique introuvable");
        }
        return toResponse(chef);
    }

    @PutMapping("/{cin}")
    public ChefInformatiqueResponse updateChefInformatique(@PathVariable String cin,
                                                            @Valid @RequestBody ChefInformatiqueRequest request) {
        ChefInformatique chef = buildChef(request);
        chef.setCin(cin);

        boolean updated = chefInformatiqueService.updateChef(chef);
        if (!updated) {
            throw new NoSuchElementException("Chef informatique introuvable");
        }
        return toResponse(chef);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteChefInformatique(@PathVariable String cin) {
        boolean removed = chefInformatiqueService.deleteChef(cin);
        if (!removed) {
            throw new NoSuchElementException("Chef informatique introuvable");
        }
        return ResponseEntity.noContent().build();
    }

    private static ChefInformatique buildChef(ChefInformatiqueRequest request) {
        return new ChefInformatique(
                request.getCin().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_CHEF_INFO
        );
    }

    private static ChefInformatiqueResponse toResponse(ChefInformatique chef) {
        return new ChefInformatiqueResponse(
                chef.getCin(),
                chef.getEmail(),
                chef.getNom(),
                chef.getPrenom(),
                chef.getRole()
        );
    }
}
