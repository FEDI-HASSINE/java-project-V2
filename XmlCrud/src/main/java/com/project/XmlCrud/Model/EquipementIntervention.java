package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipementInterventionType", propOrder = {"equipementRef", "interventionRef"})
public class EquipementIntervention {

    @XmlElement(name = "equipementRef", required = true)
    private Integer equipementRef;

    @XmlElement(name = "interventionRef", required = true)
    private Integer interventionRef;

    public EquipementIntervention() {
        // JAXB requirement
    }

    public EquipementIntervention(Integer equipementRef, Integer interventionRef) {
        this.equipementRef = equipementRef;
        this.interventionRef = interventionRef;
    }

    public Integer getEquipementRef() {
        return equipementRef;
    }

    public void setEquipementRef(Integer equipementRef) {
        this.equipementRef = equipementRef;
    }

    public Integer getInterventionRef() {
        return interventionRef;
    }

    public void setInterventionRef(Integer interventionRef) {
        this.interventionRef = interventionRef;
    }
}
