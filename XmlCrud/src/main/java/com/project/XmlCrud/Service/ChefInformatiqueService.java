package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.ChefInformatique;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChefInformatiqueService {

    public void addChefInformatique(ChefInformatique chef) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addChefInformatique(chef);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public List<ChefInformatique> getAllChefs() {
        return XmlUtil.loadMunicipalite().getChefsInformatiques();
    }

    public ChefInformatique getChefByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getChefsInformatiques()
                .stream()
                .filter(c -> c.getCin().equals(cin))
                .findFirst()
                .orElse(null);
    }

    public boolean updateChef(ChefInformatique updatedChef) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getChefsInformatiques().size(); i++) {
            ChefInformatique existing = municipalite.getChefsInformatiques().get(i);
            if (existing.getCin().equals(updatedChef.getCin())) {
                municipalite.getChefsInformatiques().set(i, updatedChef);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteChef(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.getChefsInformatiques()
                .removeIf(c -> c.getCin().equals(cin));
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
