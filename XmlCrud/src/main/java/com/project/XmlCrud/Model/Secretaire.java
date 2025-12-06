package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretaireType")
public class Secretaire extends Account {

    public Secretaire() {
        // JAXB requirement
    }

    public Secretaire(String cin, String email, String password, String nom, String prenom, String role) {
        super(cin, email, password, nom, prenom, role);
    }
}
