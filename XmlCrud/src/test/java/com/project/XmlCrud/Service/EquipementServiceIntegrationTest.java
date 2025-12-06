package com.project.XmlCrud.Service;

import com.project.XmlCrud.DTO.EquipementRequest;
import com.project.XmlCrud.Model.Equipement;
import com.project.XmlCrud.Service.XmlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EquipementServiceIntegrationTest {

    @Autowired
    private EquipementService equipementService;

    @Test
    void responsableMunicipaliteAjouteEtAssigneEquipements() throws Exception {
        Path xmlPath = Paths.get(System.getProperty("user.dir"), "src/main/resources/Schema.xml")
                .toAbsolutePath()
                .normalize();
        byte[] snapshot = Files.readAllBytes(xmlPath);

        EquipementRequest request = new EquipementRequest();
        request.setNom("Camion citerne");

        try {
            Equipement created = equipementService.addEquipement(request, "leila.updated@example.com");
            assertEquals("Camion citerne", created.getNom());
            assertTrue(created.isDisponible());

            List<Equipement> assigned = equipementService.assignEquipementsToIntervention(
                    1,
                    List.of(created.getId()),
                    "leila.updated@example.com"
            );

            assertEquals(1, assigned.size());
            assertFalse(assigned.get(0).isDisponible());

            boolean linkCreated = XmlUtil.loadMunicipalite().getEquipementInterventions().stream()
                    .anyMatch(link -> link.getEquipementRef().equals(created.getId())
                            && link.getInterventionRef().equals(1));
            assertTrue(linkCreated, "Le lien equipement-intervention doit exister");
        } finally {
            Files.write(xmlPath, snapshot);
        }
    }
}
