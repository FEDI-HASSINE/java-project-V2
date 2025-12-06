package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.ChefGeneraleRequest;
import com.project.XmlCrud.DTO.ChefGeneraleResponse;
import com.project.XmlCrud.Model.ChefGenerale;
import com.project.XmlCrud.Service.ChefGeneraleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/chefs-generaux")
public class ChefGeneraleController {

    private static final String ROLE_CHEF = "chef";

    private final ChefGeneraleService chefGeneraleService;

    public ChefGeneraleController(ChefGeneraleService chefGeneraleService) {
        this.chefGeneraleService = chefGeneraleService;
    }

    @GetMapping
    public List<ChefGeneraleResponse> getAllChefsGeneraux() {
        return chefGeneraleService.getAllChefs()
                .stream()
                .map(ChefGeneraleController::toResponse)
                .toList();
    }

    @PutMapping("/{cin}")
    public void updateChefGenerale(@PathVariable String cin,
                                    @Valid @RequestBody ChefGeneraleRequest request) {
        ChefGenerale chef = new ChefGenerale(
                cin,
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_CHEF
        );

        boolean updated = chefGeneraleService.updateChef(chef);
        if (!updated) {
            throw new NoSuchElementException("Chef général introuvable");
        }
    }

    private static ChefGeneraleResponse toResponse(ChefGenerale chef) {
        return new ChefGeneraleResponse(
                chef.getCin(),
                chef.getEmail(),
                chef.getNom(),
                chef.getPrenom(),
                chef.getRole()
        );
    }
}
