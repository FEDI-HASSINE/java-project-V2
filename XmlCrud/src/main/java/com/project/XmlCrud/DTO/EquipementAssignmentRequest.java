package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class EquipementAssignmentRequest {

    @NotEmpty
    private List<Integer> equipementIds;

    public List<Integer> getEquipementIds() {
        return equipementIds;
    }

    public void setEquipementIds(List<Integer> equipementIds) {
        this.equipementIds = equipementIds;
    }
}
