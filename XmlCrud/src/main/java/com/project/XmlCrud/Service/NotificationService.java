package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Citoyen;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.Model.Notification;
import com.project.XmlCrud.Model.Secretaire;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class NotificationService {

    private final SecretaireService secretaireService;
    private final CitoyenService citoyenService;

    public NotificationService(SecretaireService secretaireService, CitoyenService citoyenService) {
        this.secretaireService = secretaireService;
        this.citoyenService = citoyenService;
    }

    public void addNotification(Notification notification) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        municipalite.addNotification(notification);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public Notification createNotificationForDemande(Integer demandeId, String contenue, String secretaireEmail) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        Secretaire secretaire = secretaireService.getSecretaireByEmail(normalizeEmail(secretaireEmail))
                .orElseThrow(() -> new IllegalArgumentException("Secretaire introuvable pour l'utilisateur connecté"));

        Demande demande = municipalite.getDemandes().stream()
                .filter(d -> demandeId.equals(d.getIdentifiant()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Demande introuvable"));

        Citoyen citoyen = citoyenService.getCitoyenByCIN(demande.getCitoyenRef());
        if (citoyen == null) {
            throw new IllegalStateException("Citoyen introuvable pour la demande");
        }

        String cleanedContenue = cleanContenue(contenue);

        int nextId = municipalite.getNotifications().stream()
                .map(Notification::getIdN)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Notification notification = new Notification();
        notification.setIdN(nextId);
        notification.setCitoyenRef(citoyen.getCin());
        notification.setContenue(cleanedContenue);

        municipalite.addNotification(notification);
        XmlUtil.saveMunicipalite(municipalite);
        return notification;
    }

    public List<Notification> getAllNotifications() {
        return XmlUtil.loadMunicipalite().getNotifications();
    }

    public List<Notification> getNotificationsForCitizen(String email) {
        String normalized = normalizeEmail(email);
        Citoyen citoyen = citoyenService.getCitoyenByEmail(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable pour l'utilisateur connecté"));

        return XmlUtil.loadMunicipalite().getNotifications().stream()
                .filter(n -> citoyen.getCin().equals(n.getCitoyenRef()))
                .toList();
    }

    public Notification getNotificationById(Integer idN) {
        return XmlUtil.loadMunicipalite().getNotifications()
                .stream()
                .filter(n -> n.getIdN().equals(idN))
                .findFirst()
                .orElse(null);
    }

    public Notification getNotificationForCitizen(Integer idN, String email) {
        String normalized = normalizeEmail(email);
        Citoyen citoyen = citoyenService.getCitoyenByEmail(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable pour l'utilisateur connecté"));

        return XmlUtil.loadMunicipalite().getNotifications().stream()
                .filter(n -> idN.equals(n.getIdN()) && citoyen.getCin().equals(n.getCitoyenRef()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Notification introuvable"));
    }

    public void deleteNotificationForCitizen(Integer idN, String email) {
        String normalized = normalizeEmail(email);
        Citoyen citoyen = citoyenService.getCitoyenByEmail(normalized)
                .orElseThrow(() -> new IllegalArgumentException("Citoyen introuvable pour l'utilisateur connecté"));

        Municipalite municipalite = XmlUtil.loadMunicipalite();

        Notification notification = municipalite.getNotifications().stream()
                .filter(n -> idN.equals(n.getIdN()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Notification introuvable"));

        if (!citoyen.getCin().equals(notification.getCitoyenRef())) {
            throw new IllegalArgumentException("Le citoyen ne peut supprimer que ses propres notifications");
        }

        municipalite.removeNotificationById(idN);
        XmlUtil.saveMunicipalite(municipalite);
    }

    public boolean updateNotification(Notification updatedNotification) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        for (int i = 0; i < municipalite.getNotifications().size(); i++) {
            Notification existing = municipalite.getNotifications().get(i);
            if (existing.getIdN().equals(updatedNotification.getIdN())) {
                municipalite.getNotifications().set(i, updatedNotification);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteNotification(Integer idN) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.removeNotificationById(idN);
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static String cleanContenue(String contenue) {
        String cleaned = contenue == null ? "" : contenue.trim();
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Le contenu de la notification est obligatoire");
        }
        return cleaned;
    }
}
