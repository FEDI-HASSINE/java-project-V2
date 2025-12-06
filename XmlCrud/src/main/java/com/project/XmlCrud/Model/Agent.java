package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgentType", propOrder = {"disponibilite", "service"})
public class Agent extends Account {

    @XmlElement(name = "Disponibilite", required = true)
    private boolean disponibilite;

    @XmlElement(name = "service", required = true)
    private String service;

    public Agent() {
        // JAXB requirement
    }

    public Agent(String cin, String email, String password, String nom, String prenom, String role,
                 boolean disponibilite, String service) {
        super(cin, email, password, nom, prenom, role);
        this.disponibilite = disponibilite;
        this.service = service;
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
}
