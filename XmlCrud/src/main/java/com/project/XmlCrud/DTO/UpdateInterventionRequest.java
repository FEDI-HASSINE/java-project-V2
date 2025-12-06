package com.project.XmlCrud.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class UpdateInterventionRequest {

    @NotNull @Min(0)
    private Integer budget;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @NotBlank
    private String type;

    @NotNull @Min(0)
    private Integer urgence;

    @Min(0)
    private Integer etat;

    private String localisation;

    @NotBlank
    private String agentCin;

    // Remplacement complet des équipements liés
    private List<Integer> equipementIds;

    public Integer getBudget() { return budget; }
    public void setBudget(Integer budget) { this.budget = budget; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getUrgence() { return urgence; }
    public void setUrgence(Integer urgence) { this.urgence = urgence; }
    public Integer getEtat() { return etat; }
    public void setEtat(Integer etat) { this.etat = etat; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getAgentCin() { return agentCin; }
    public void setAgentCin(String agentCin) { this.agentCin = agentCin; }
    public List<Integer> getEquipementIds() { return equipementIds; }
    public void setEquipementIds(List<Integer> equipementIds) { this.equipementIds = equipementIds; }
}
