package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.RapportDeTravaille;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RapportDeTravailleService {

    public void addRapport(RapportDeTravaille rapport) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addRapport(rapport);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public List<RapportDeTravaille> getAllRapports() {
        return XmlUtil.loadMunicipalite().getRapportsDeTravaille();
    }

    public RapportDeTravaille getRapportByInterventionRef(Integer ref) {
        return XmlUtil.loadMunicipalite().getRapportsDeTravaille()
                .stream()
                .filter(r -> r.getInterventionRef().equals(ref))
                .findFirst()
                .orElse(null);
    }

    public boolean updateRapport(RapportDeTravaille updatedRapport) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getRapportsDeTravaille().size(); i++) {
            RapportDeTravaille existing = municipalite.getRapportsDeTravaille().get(i);
            if (existing.getInterventionRef().equals(updatedRapport.getInterventionRef())) {
                municipalite.getRapportsDeTravaille().set(i, updatedRapport);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteRapport(Integer interventionRef) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.removeRapportByInterventionRef(interventionRef);
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
