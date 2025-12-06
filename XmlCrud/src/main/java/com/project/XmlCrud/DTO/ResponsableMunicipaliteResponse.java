package com.project.XmlCrud.DTO;

public class ResponsableMunicipaliteResponse {

    private String cin;
    private String email;
    private String nom;
    private String prenom;
    private String role;

    public ResponsableMunicipaliteResponse() {
        // Default constructor for serialization frameworks
    }

    public ResponsableMunicipaliteResponse(String cin, String email, String nom, String prenom, String role) {
        this.cin = cin;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
