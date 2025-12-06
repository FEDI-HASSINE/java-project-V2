package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponsableMunicipaliteType")
public class ResponsableMunicipalite extends Account {

    public ResponsableMunicipalite() {
        // JAXB requirement
    }

    public ResponsableMunicipalite(String cin, String email, String password, String nom, String prenom, String role) {
        super(cin, email, password, nom, prenom, role);
    }
}
