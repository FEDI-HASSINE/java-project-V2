package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChefGeneraleType")
public class ChefGenerale extends Account {

    public ChefGenerale() {
        // JAXB requirement
    }

    public ChefGenerale(String cin, String email, String password, String nom, String prenom, String role) {
        super(cin, email, password, nom, prenom, role);
    }
}
