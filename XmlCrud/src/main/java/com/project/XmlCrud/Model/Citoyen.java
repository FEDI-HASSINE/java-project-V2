package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CitoyenType", propOrder = {"adresse", "rue"})
public class Citoyen extends Account {

    @XmlElement(name = "Adresse", required = true)
    private String adresse;

    @XmlElement(name = "Rue", required = true)
    private String rue;

    public Citoyen() {
        // JAXB requirement
    }

    public Citoyen(String cin, String email, String password, String nom, String prenom, String role,
                    String adresse, String rue) {
        super(cin, email, password, nom, prenom, role);
        this.adresse = adresse;
        this.rue = rue;
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
}
