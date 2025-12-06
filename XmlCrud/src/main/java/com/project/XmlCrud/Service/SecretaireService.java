package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.Secretaire;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecretaireService {

    public Secretaire addSecretaire(Secretaire secretaire) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean exists = municipalite.getSecretaires().stream()
                .anyMatch(existing -> existing.getCin().equals(secretaire.getCin())
                        || existing.getEmail().equalsIgnoreCase(secretaire.getEmail()));
        if (exists) {
            throw new IllegalArgumentException("Secretaire avec ce CIN ou email existe déjà");
        }

        municipalite.addSecretaire(secretaire);
        XmlUtil.saveMunicipalite(municipalite);
        return secretaire;
    }

    public List<Secretaire> getAllSecretaires() {
        return XmlUtil.loadMunicipalite().getSecretaires();
    }

    public Optional<Secretaire> getSecretaireByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getSecretaires()
                .stream()
                .filter(s -> s.getCin().equals(cin))
                .findFirst();
    }

    public Optional<Secretaire> getSecretaireByEmail(String email) {
        return XmlUtil.loadMunicipalite().getSecretaires()
                .stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean updateSecretaire(Secretaire updatedSecretaire) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean emailTaken = municipalite.getSecretaires().stream()
                .anyMatch(existing -> !existing.getCin().equals(updatedSecretaire.getCin())
                        && existing.getEmail().equalsIgnoreCase(updatedSecretaire.getEmail()));
        if (emailTaken) {
            throw new IllegalArgumentException("Adresse email déjà utilisée par un autre secretaire");
        }

        for (int i = 0; i < municipalite.getSecretaires().size(); i++) {
            Secretaire existing = municipalite.getSecretaires().get(i);
            if (existing.getCin().equals(updatedSecretaire.getCin())) {
                municipalite.getSecretaires().set(i, updatedSecretaire);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteSecretaire(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.getSecretaires()
                .removeIf(s -> s.getCin().equals(cin));
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
