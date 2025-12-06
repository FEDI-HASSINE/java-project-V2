package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateInterventionEtatRequest {

    @NotNull(message = "etat est requis")
    @Min(value = 0, message = "etat doit être positif")
    @Max(value = 100, message = "etat doit être au plus 100")
    private Integer etat;

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }
}
