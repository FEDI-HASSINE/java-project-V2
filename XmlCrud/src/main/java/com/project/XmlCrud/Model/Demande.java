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
@XmlType(name = "DemandeType", propOrder = {
        "identifiant",
        "citoyenRef",
        "dateDeSoumission",
        "image",
        "localisation"
})
public class Demande {

    @XmlElement(name = "identifiant", required = true)
    private Integer identifiant;

    @XmlElement(name = "citoyenRef", required = true)
    private String citoyenRef;

    @XmlElement(name = "DateDeSoumission", required = true)
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlSchemaType(name = "date")
    private LocalDate dateDeSoumission;

    @XmlElement(name = "image", required = true)
    private byte[] image = new byte[0];

    @XmlElement(name = "localisation", required = true)
    private String localisation;

    public Demande() {
        // JAXB requirement
    }

    public Demande(Integer identifiant, String citoyenRef, LocalDate dateDeSoumission, byte[] image, String localisation) {
        this.identifiant = identifiant;
        this.citoyenRef = citoyenRef;
        this.dateDeSoumission = dateDeSoumission;
        this.image = image == null ? new byte[0] : image;
        this.localisation = localisation;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getCitoyenRef() {
        return citoyenRef;
    }

    public void setCitoyenRef(String citoyenRef) {
        this.citoyenRef = citoyenRef;
    }

    public LocalDate getDateDeSoumission() {
        return dateDeSoumission;
    }

    public void setDateDeSoumission(LocalDate dateDeSoumission) {
        this.dateDeSoumission = dateDeSoumission;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image == null ? new byte[0] : image;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
