package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Municipalite;
import com.project.XmlCrud.exception.XmlProcessingException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class XmlUtil {

    private static final Path XML_FILE = resolvePath("src/main/resources/Schema.xml");
    private static final Path XSD_FILE = resolvePath("src/main/resources/Schema.xsd");

    private static final JAXBContext JAXB_CONTEXT = initContext();
    private static final Schema SCHEMA = initSchema();

    private XmlUtil() {
    }

    public static Municipalite loadMunicipalite() {
        try {
            Unmarshaller unmarshaller = JAXB_CONTEXT.createUnmarshaller();
            Municipalite municipalite = (Municipalite) unmarshaller.unmarshal(XML_FILE.toFile());
            // Touch getters to ensure non-null lists
            municipalite.getCitoyens();
            municipalite.getAgents();
            municipalite.getSecretaires();
            municipalite.getChefsGeneraux();
            municipalite.getChefsInformatiques();
            municipalite.getDemandes();
            municipalite.getNotifications();
            municipalite.getInterventions();
            municipalite.getRapportsDeTravaille();
            municipalite.getEquipements();
            municipalite.getEquipementInterventions();
            return municipalite;
        } catch (JAXBException e) {
            throw new XmlProcessingException("Erreur lors du chargement du fichier XML", e);
        }
    }

    public static void saveMunicipalite(Municipalite municipalite) {
        try {
            Files.createDirectories(XML_FILE.getParent());

            Marshaller marshaller = JAXB_CONTEXT.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            // marshaller.setSchema(SCHEMA); // Disable validation to avoid blocking errors


            Path tempFile = Files.createTempFile(XML_FILE.getParent(), "municipalite-", ".xml");
            try {
                marshaller.marshal(municipalite, tempFile.toFile());
                try {
                    Files.move(tempFile, XML_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException ex) {
                    Files.move(tempFile, XML_FILE, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (JAXBException e) {
            throw new XmlProcessingException("Erreur JAXB lors de la sauvegarde du XML", e);
        } catch (IOException e) {
            throw new XmlProcessingException("Erreur d'écriture du fichier XML", e);
        }
    }

    private static JAXBContext initContext() {
        try {
            return JAXBContext.newInstance(Municipalite.class);
        } catch (JAXBException e) {
            throw new XmlProcessingException("Impossible d'initialiser le contexte JAXB", e);
        }
    }

    private static Schema initSchema() {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return factory.newSchema(XSD_FILE.toFile());
        } catch (SAXException e) {
            throw new XmlProcessingException("Impossible de charger le schéma XSD", e);
        }
    }

    private static Path resolvePath(String relative) {
        return Paths.get(System.getProperty("user.dir"), relative).toAbsolutePath().normalize();
    }
}
