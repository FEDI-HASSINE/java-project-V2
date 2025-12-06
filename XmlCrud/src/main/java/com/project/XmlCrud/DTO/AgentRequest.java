package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AgentRequest {

    @NotBlank
    @Pattern(regexp = "\\d{8}", message = "Le CIN doit contenir 8 chiffres")
    private String cin;

    @NotBlank
    @Email
    private String email;

    // Password validation is handled manually in the controller
    private String password;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    // Disponibilite is optional for updates (handled in controller)
    private Boolean disponibilite;

    @NotBlank
    private String service;

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
