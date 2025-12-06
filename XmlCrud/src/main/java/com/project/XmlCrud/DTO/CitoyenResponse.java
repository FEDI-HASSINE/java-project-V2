package com.project.XmlCrud.DTO;

public class CitoyenResponse {

    private String cin;
    private String email;
    private String nom;
    private String prenom;
    private String adresse;
    private String rue;
    private String role;

    public CitoyenResponse() {
        // Default constructor for serializers
    }

    public CitoyenResponse(String cin, String email, String nom, String prenom,
                            String adresse, String rue, String role) {
        this.cin = cin;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.rue = rue;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
