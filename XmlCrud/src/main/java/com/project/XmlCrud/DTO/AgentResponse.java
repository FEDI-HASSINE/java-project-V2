package com.project.XmlCrud.DTO;

public class AgentResponse {

    private String cin;
    private String email;
    private String nom;
    private String prenom;
    private boolean disponibilite;
    private String service;
    private String role;

    public AgentResponse() {
        // Default constructor for mappers/serializers
    }

    public AgentResponse(String cin, String email, String nom, String prenom,
                         boolean disponibilite, String service, String role) {
        this.cin = cin;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.disponibilite = disponibilite;
        this.service = service;
        this.role = role;
    }

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

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
