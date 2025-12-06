package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipementType", propOrder = {"id", "nom", "disponible"})
public class Equipement {

    @XmlElement(name = "id", required = true)
    private Integer id;

    @XmlElement(name = "nom", required = true)
    private String nom;

    @XmlElement(name = "disponible", required = true)
    private boolean disponible;

    public Equipement() {
        // JAXB requirement
    }

    public Equipement(Integer id, String nom, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.disponible = disponible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
