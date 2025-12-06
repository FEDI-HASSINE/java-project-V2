package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationType", propOrder = {"idN", "citoyenRef", "contenue"})
public class Notification {

    @XmlElement(name = "idN", required = true)
    private Integer idN;

    @XmlElement(name = "citoyenRef", required = true)
    private String citoyenRef;

    @XmlElement(name = "contenue", required = true)
    private String contenue;

    public Notification() {
        // JAXB requirement
    }

    public Notification(Integer idN, String citoyenRef, String contenue) {
        this.idN = idN;
        this.citoyenRef = citoyenRef;
        this.contenue = contenue;
    }

    public Integer getIdN() {
        return idN;
    }

    public void setIdN(Integer idN) {
        this.idN = idN;
    }

    public String getCitoyenRef() {
        return citoyenRef;
    }

    public void setCitoyenRef(String citoyenRef) {
        this.citoyenRef = citoyenRef;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }
}
