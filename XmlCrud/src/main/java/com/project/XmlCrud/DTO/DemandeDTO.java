package com.project.XmlCrud.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class DemandeDTO {

    @NotBlank(message = "La référence citoyen est obligatoire")
    @Pattern(regexp = "\\d{8}", message = "La référence citoyen doit contenir 8 chiffres")
    @JsonAlias({"citoyenCinRef", "citoyenRef"})
    private String citoyenCin;

    @NotBlank(message = "La localisation est obligatoire")
    @JsonAlias({"emplacement"})
    private String localisation;

    @JsonAlias({"photo", "image"})
    private String imageBase64;

    private String description;
    private String etat;
    private String municipaliteNom;

    public String getCitoyenCin() {
        return citoyenCin;
    }

    public void setCitoyenCin(String citoyenCin) {
        this.citoyenCin = citoyenCin;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getMunicipaliteNom() {
        return municipaliteNom;
    }

    public void setMunicipaliteNom(String municipaliteNom) {
        this.municipaliteNom = municipaliteNom;
    }
}
