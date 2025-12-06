package com.project.XmlCrud.Service;

import com.project.XmlCrud.DTO.DemandeDTO;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class DemandeService {

    private final CitoyenService citoyenService;

    public DemandeService(CitoyenService citoyenService) {
        this.citoyenService = citoyenService;
    }

    public Demande createDemande(DemandeDTO demandeDTO) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        String citoyenCin = demandeDTO.getCitoyenCin().trim();
        boolean citoyenExiste = municipalite.getCitoyens().stream()
            .anyMatch(c -> c.getCin().equals(citoyenCin));

        if (!citoyenExiste) {
            throw new IllegalArgumentException("Citoyen introuvable pour le CIN fourni");
        }

        int nextId = municipalite.getDemandes().stream()
                .map(Demande::getIdentifiant)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Demande demande = new Demande();
        demande.setIdentifiant(nextId);
        demande.setCitoyenRef(citoyenCin);
        demande.setDateDeSoumission(LocalDate.now());
        demande.setImage(decodeBase64OrEmpty(demandeDTO.getImageBase64()));
        demande.setLocalisation(demandeDTO.getLocalisation());

        municipalite.addDemande(demande);
        XmlUtil.saveMunicipalite(municipalite);

        return demande;
    }

    public List<Demande> getAllDemandes() {
        return XmlUtil.loadMunicipalite().getDemandes();
    }

    public List<Demande> getDemandesForCitizen(String email) {
        String trimmedEmail = email == null ? "" : email.trim();
        var citoyen = citoyenService.getCitoyenByEmail(trimmedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable pour l'utilisateur connecté"));

        return XmlUtil.loadMunicipalite().getDemandes().stream()
                .filter(d -> citoyen.getCin().equals(d.getCitoyenRef()))
                .toList();
    }

    public Demande getDemandeById(Integer id) {
        return XmlUtil.loadMunicipalite().getDemandes().stream()
                .filter(d -> id.equals(d.getIdentifiant()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Demande introuvable"));
    }

    public Demande getDemandeForCitizen(Integer id, String email) {
        String trimmedEmail = email == null ? "" : email.trim();
        var citoyen = citoyenService.getCitoyenByEmail(trimmedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable pour l'utilisateur connecté"));

        return XmlUtil.loadMunicipalite().getDemandes().stream()
                .filter(d -> id.equals(d.getIdentifiant()) && citoyen.getCin().equals(d.getCitoyenRef()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Demande introuvable"));
    }

    public void deleteDemande(Integer id) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        if (!municipalite.removeDemandeById(id)) {
            throw new NoSuchElementException("Demande introuvable");
        }
        XmlUtil.saveMunicipalite(municipalite);
    }

    private static byte[] decodeBase64OrEmpty(String value) {
        if (value == null || value.isBlank()) {
            return new byte[0];
        }
        try {
            return Base64.getDecoder().decode(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Image fournie n'est pas au format Base64 valide", ex);
        }
    }
}
