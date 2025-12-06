package com.project.XmlCrud.Model;

import com.project.XmlCrud.Model.adapter.LocalDateAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterventionType", propOrder = {
    "id",
    "budget",
    "dateDebut",
    "type",
    "urgence",
    "etat",
    "localisation",
    "image",
    "cinAgent",
    "cinSecretaire"
})
public class Intervention {

    @XmlElement(name = "id", required = true)
    private Integer id;

    @XmlElement(name = "budget", required = true)
    private Integer budget;

    @XmlElement(name = "dateDebut", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlSchemaType(name = "date")
    private LocalDate dateDebut;

    @XmlElement(name = "type", required = true)
    private String type;

    @XmlElement(name = "urgence", required = true)
    private Integer urgence;

    @XmlElement(name = "etat", required = true)
    private Integer etat = 0;

    @XmlElement(name = "localisation", required = true)
    private String localisation = "";

    @XmlElement(name = "image", required = true)
    private byte[] image = new byte[0];

    @XmlElement(name = "CINAgent", required = true)
    private String cinAgent;

    @XmlElement(name = "CINSecretaire", required = true)
    private String cinSecretaire;

    public Intervention() {
        // JAXB requirement
    }

    public Intervention(Integer id, Integer budget, LocalDate dateDebut, String type,
                        Integer urgence, Integer etat, String localisation, byte[] image,
                        String cinAgent, String cinSecretaire) {
        this.id = id;
        this.budget = budget;
        this.dateDebut = dateDebut;
        this.type = type;
        this.urgence = urgence;
        this.etat = etat == null ? 0 : etat;
        this.localisation = localisation == null ? "" : localisation;
        this.image = image == null ? new byte[0] : image;
        this.cinAgent = cinAgent;
        this.cinSecretaire = cinSecretaire;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUrgence() {
        return urgence;
    }

    public void setUrgence(Integer urgence) {
        this.urgence = urgence;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat == null ? 0 : etat;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation == null ? "" : localisation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image == null ? new byte[0] : image;
    }

    public String getCinAgent() {
        return cinAgent;
    }

    public void setCinAgent(String cinAgent) {
        this.cinAgent = cinAgent;
    }

    public String getCinSecretaire() {
        return cinSecretaire;
    }

    public void setCinSecretaire(String cinSecretaire) {
        this.cinSecretaire = cinSecretaire;
    }
}
