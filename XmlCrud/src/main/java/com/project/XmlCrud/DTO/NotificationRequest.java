package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {

    @NotBlank
    private String contenue;

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }
}
