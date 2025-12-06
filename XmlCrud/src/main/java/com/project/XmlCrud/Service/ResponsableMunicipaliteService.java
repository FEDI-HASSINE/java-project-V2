package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.ResponsableMunicipalite;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsableMunicipaliteService {

    public ResponsableMunicipalite addResponsableMunicipalite(ResponsableMunicipalite responsableMunicipalite) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean exists = municipalite.getResponsableMunicipalites().stream()
                .anyMatch(existing -> existing.getCin().equals(responsableMunicipalite.getCin())
                        || existing.getEmail().equalsIgnoreCase(responsableMunicipalite.getEmail()));
        if (exists) {
            throw new IllegalArgumentException("ResponsableMunicipalite avec ce CIN ou email existe déjà");
        }

        municipalite.addResponsableMunicipalite(responsableMunicipalite);
        XmlUtil.saveMunicipalite(municipalite);
        return responsableMunicipalite;
    }

    public List<ResponsableMunicipalite> getAllResponsableMunicipalites() {
        return XmlUtil.loadMunicipalite().getResponsableMunicipalites();
    }

    public Optional<ResponsableMunicipalite> getResponsableMunicipaliteByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getResponsableMunicipalites()
                .stream()
                .filter(s -> s.getCin().equals(cin))
                .findFirst();
    }

    public Optional<ResponsableMunicipalite> getResponsableMunicipaliteByEmail(String email) {
        return XmlUtil.loadMunicipalite().getResponsableMunicipalites()
                .stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean updateResponsableMunicipalite(ResponsableMunicipalite updatedResponsableMunicipalite) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean emailTaken = municipalite.getResponsableMunicipalites().stream()
                .anyMatch(existing -> !existing.getCin().equals(updatedResponsableMunicipalite.getCin())
                        && existing.getEmail().equalsIgnoreCase(updatedResponsableMunicipalite.getEmail()));
        if (emailTaken) {
            throw new IllegalArgumentException("Adresse email déjà utilisée par un autre responsableMunicipalite");
        }

        for (int i = 0; i < municipalite.getResponsableMunicipalites().size(); i++) {
            ResponsableMunicipalite existing = municipalite.getResponsableMunicipalites().get(i);
            if (existing.getCin().equals(updatedResponsableMunicipalite.getCin())) {
                municipalite.getResponsableMunicipalites().set(i, updatedResponsableMunicipalite);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteResponsableMunicipalite(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.getResponsableMunicipalites()
                .removeIf(s -> s.getCin().equals(cin));
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
