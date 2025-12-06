package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.NotBlank;

public class EquipementRequest {

    @NotBlank
    private String nom;

    private Boolean disponible;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
