package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Citoyen;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitoyenService {

    public void addCitoyen(Citoyen citoyen) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addCitoyen(citoyen);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public List<Citoyen> getAllCitoyens() {
        return XmlUtil.loadMunicipalite().getCitoyens();
    }

    public Citoyen getCitoyenByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getCitoyens()
                .stream()
                .filter(c -> c.getCin().equals(cin))
                .findFirst()
                .orElse(null);
    }

    public Optional<Citoyen> getCitoyenByEmail(String email) {
        return XmlUtil.loadMunicipalite().getCitoyens()
                .stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean updateCitoyen(Citoyen updatedCitoyen) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getCitoyens().size(); i++) {
            Citoyen existing = municipalite.getCitoyens().get(i);
            if (existing.getCin().equals(updatedCitoyen.getCin())) {
                municipalite.getCitoyens().set(i, updatedCitoyen);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCitoyen(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.removeCitoyenByCin(cin);
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
