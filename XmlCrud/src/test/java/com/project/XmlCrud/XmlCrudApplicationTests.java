package com.project.XmlCrud;

import com.project.XmlCrud.DTO.InterventionFromDemandeRequest;
import com.project.XmlCrud.Model.Demande;
import com.project.XmlCrud.Model.Intervention;
import com.project.XmlCrud.Service.DemandeService;
import com.project.XmlCrud.Service.InterventionService;
import com.project.XmlCrud.Service.XmlUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class XmlCrudApplicationTests {

	@Autowired
	private DemandeService demandeService;

	@Autowired
	private InterventionService interventionService;

	@Test
	void contextLoads() {
	}

	@Test
	void citizenSeesOnlyOwnDemandes() {
		List<Demande> demandes = demandeService.getDemandesForCitizen("nour.citizen@example.com");
		assertFalse(demandes.isEmpty(), "Le citoyen devrait avoir des demandes associées");
		assertEquals(2, demandes.size(), "Le citoyen Nour doit avoir exactement 2 demandes");
		assertTrue(demandes.stream().allMatch(d -> "98765432".equals(d.getCitoyenRef())),
				"Toutes les demandes doivent appartenir au citoyen Nour");
	}

	@Test
	void responsableMunicipaliteCreatesInterventionFromDemande() throws Exception {
		Path xmlPath = Paths.get(System.getProperty("user.dir"), "src/main/resources/Schema.xml").toAbsolutePath().normalize();
		byte[] snapshot = Files.readAllBytes(xmlPath);
		int baseCount = XmlUtil.loadMunicipalite().getInterventions().size();

		InterventionFromDemandeRequest request = new InterventionFromDemandeRequest();
		request.setBudget(750);
		request.setDateDebut(LocalDate.now());
		request.setType("Standard");
		request.setUrgence(2);
		request.setEtat(5);
		request.setLocalisation("Zone Test");
		request.setImageBase64("");
		request.setAgentCin("11112222");

		try {
			Intervention created = interventionService.createInterventionFromDemande(
					1,
					request,
					"leila.updated@example.com"
			);
			assertEquals("11112222", created.getCinAgent(), "L'intervention doit être assignée à l'agent fourni");
			assertEquals("22223333", created.getCinResponsableMunicipalite(), "L'intervention doit référencer la responsableMunicipalite connectée");
			int updatedCount = XmlUtil.loadMunicipalite().getInterventions().size();
			assertEquals(baseCount + 1, updatedCount, "Une intervention doit être ajoutée");
		} finally {
			Files.write(xmlPath, snapshot);
		}
	}
}
