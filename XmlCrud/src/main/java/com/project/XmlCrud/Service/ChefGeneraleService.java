package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.ChefGenerale;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChefGeneraleService {

    public void addChefGenerale(ChefGenerale chef) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addChefGenerale(chef);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public List<ChefGenerale> getAllChefs() {
        return XmlUtil.loadMunicipalite().getChefsGeneraux();
    }

    public ChefGenerale getChefByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getChefsGeneraux()
                .stream()
                .filter(c -> c.getCin().equals(cin))
                .findFirst()
                .orElse(null);
    }

    public boolean updateChef(ChefGenerale updatedChef) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getChefsGeneraux().size(); i++) {
            ChefGenerale existing = municipalite.getChefsGeneraux().get(i);
            if (existing.getCin().equals(updatedChef.getCin())) {
                municipalite.getChefsGeneraux().set(i, updatedChef);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteChef(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.getChefsGeneraux().removeIf(c -> c.getCin().equals(cin));
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
