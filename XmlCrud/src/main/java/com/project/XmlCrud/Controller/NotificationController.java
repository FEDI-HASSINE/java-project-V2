package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.NotificationRequest;
import com.project.XmlCrud.Model.Notification;
import com.project.XmlCrud.Service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    private static final String ROLE_SECRETAIRE = "secretaire";
    private static final String ROLE_CITOYEN = "citoyen";

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/demandes/{demandeId}/notifications")
    public ResponseEntity<Notification> createNotification(@PathVariable Integer demandeId,
                                                           @Valid @RequestBody NotificationRequest request,
                                                           Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_SECRETAIRE)) {
            throw new AccessDeniedException("Seule une secretaire peut créer une notification pour une demande");
        }

        Notification created = notificationService.createNotificationForDemande(
                demandeId,
                request.getContenue(),
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/notifications")
    public List<Notification> getallNotifications(Authentication authentication) {
        if (hasAuthority(authentication, ROLE_CITOYEN)) {
            return notificationService.getNotificationsForCitizen(authentication.getName());
        }
        if (hasAuthority(authentication, ROLE_SECRETAIRE)) {
            return notificationService.getAllNotifications();
        }
        throw new AccessDeniedException("Accès limité aux citoyens et aux secretaires");
    }

    @GetMapping("/notifications/{id}")
    public Notification getNotificationbyid(@PathVariable Integer id, Authentication authentication) {
        if (hasAuthority(authentication, ROLE_CITOYEN)) {
            return notificationService.getNotificationForCitizen(id, authentication.getName());
        }
        if (!hasAuthority(authentication, ROLE_SECRETAIRE)) {
            throw new AccessDeniedException("Accès limité aux citoyens et aux secretaires");
        }

        Notification notification = notificationService.getNotificationById(id);
        if (notification == null) {
            throw new java.util.NoSuchElementException("Notification introuvable");
        }
        return notification;
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id, Authentication authentication) {
        if (!hasAuthority(authentication, ROLE_CITOYEN)) {
            throw new AccessDeniedException("Seul un citoyen peut supprimer une notification");
        }

        notificationService.deleteNotificationForCitizen(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    private static boolean hasAuthority(Authentication authentication, String authority) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> authority.equalsIgnoreCase(auth));
    }
}
