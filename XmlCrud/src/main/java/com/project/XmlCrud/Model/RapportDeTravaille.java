package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RapportDeTravailleType", propOrder = {
        "cinAgent",
        "cout",
        "duree",
        "description",
        "image",
        "interventionRef"
})
public class RapportDeTravaille {

    @XmlElement(name = "CINAgent", required = true)
    private String cinAgent;

    @XmlElement(name = "cout", required = true)
    private Integer cout;

    @XmlElement(name = "Duree", required = true)
    private Integer duree;

    @XmlElement(name = "description", required = true)
    private String description;

    @XmlElement(name = "image", required = true)
    private byte[] image = new byte[0];

    @XmlElement(name = "InterventionRef", required = true)
    private Integer interventionRef;

    public RapportDeTravaille() {
        // JAXB requirement
    }

    public RapportDeTravaille(String cinAgent, Integer cout, Integer duree,
                              String description, byte[] image, Integer interventionRef) {
        this.cinAgent = cinAgent;
        this.cout = cout;
        this.duree = duree;
        this.description = description;
        this.image = image == null ? new byte[0] : image;
        this.interventionRef = interventionRef;
    }

    public String getCinAgent() {
        return cinAgent;
    }

    public void setCinAgent(String cinAgent) {
        this.cinAgent = cinAgent;
    }

    public Integer getCout() {
        return cout;
    }

    public void setCout(Integer cout) {
        this.cout = cout;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image == null ? new byte[0] : image;
    }

    public Integer getInterventionRef() {
        return interventionRef;
    }

    public void setInterventionRef(Integer interventionRef) {
        this.interventionRef = interventionRef;
    }
}
