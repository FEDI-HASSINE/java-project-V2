package com.project.XmlCrud.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Municipalite")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "citoyens",
        "agents",
        "responsableMunicipalites",
        "chefsGeneraux",
        "chefsInformatiques",
        "demandes",
        "notifications",
        "interventions",
        "rapportsDeTravaille",
        "equipements",
        "equipementInterventions"
})
public class Municipalite {

    @XmlElementWrapper(name = "Citoyens", required = true)
    @XmlElement(name = "Citoyen", required = true)
    private List<Citoyen> citoyens = new ArrayList<>();

    @XmlElementWrapper(name = "Agents", required = true)
    @XmlElement(name = "Agent", required = true)
    private List<Agent> agents = new ArrayList<>();

    @XmlElementWrapper(name = "ResponsablesMunicipalites", required = true)
    @XmlElement(name = "ResponsableMunicipalite", required = true)
    private List<ResponsableMunicipalite> responsableMunicipalites = new ArrayList<>();

    @XmlElementWrapper(name = "ChefsGeneraux", required = true)
    @XmlElement(name = "ChefGenerale", required = true)
    private List<ChefGenerale> chefsGeneraux = new ArrayList<>();

    @XmlElementWrapper(name = "ChefsInformatiques", required = true)
    @XmlElement(name = "ChefInformatique", required = true)
    private List<ChefInformatique> chefsInformatiques = new ArrayList<>();

    @XmlElementWrapper(name = "Demandes", required = true)
    @XmlElement(name = "Demande", required = true)
    private List<Demande> demandes = new ArrayList<>();

    @XmlElementWrapper(name = "Notifications", required = true)
    @XmlElement(name = "Notification", required = true)
    private List<Notification> notifications = new ArrayList<>();

    @XmlElementWrapper(name = "Interventions", required = true)
    @XmlElement(name = "Intervention", required = true)
    private List<Intervention> interventions = new ArrayList<>();

    @XmlElementWrapper(name = "RapportsDeTravaille", required = true)
    @XmlElement(name = "RapportDeTravaille", required = true)
    private List<RapportDeTravaille> rapportsDeTravaille = new ArrayList<>();

    @XmlElementWrapper(name = "Equipements", required = true)
    @XmlElement(name = "Equipement", required = true)
    private List<Equipement> equipements = new ArrayList<>();

    @XmlElementWrapper(name = "EquipementInterventions", required = true)
    @XmlElement(name = "EquipementIntervention", required = true)
    private List<EquipementIntervention> equipementInterventions = new ArrayList<>();

    public Municipalite() {
        // JAXB requirement
    }

    // region Getters / Setters ---------------------------------------------------------------

    public List<Citoyen> getCitoyens() {
        return ensureList(citoyens);
    }

    public void setCitoyens(List<Citoyen> citoyens) {
        this.citoyens = ensureList(citoyens);
    }

    public List<Agent> getAgents() {
        return ensureList(agents);
    }

    public void setAgents(List<Agent> agents) {
        this.agents = ensureList(agents);
    }

    public List<ResponsableMunicipalite> getResponsableMunicipalites() {
        return ensureList(responsableMunicipalites);
    }

    public void setResponsableMunicipalites(List<ResponsableMunicipalite> responsableMunicipalites) {
        this.responsableMunicipalites = ensureList(responsableMunicipalites);
    }

    public List<ChefGenerale> getChefsGeneraux() {
        return ensureList(chefsGeneraux);
    }

    public void setChefsGeneraux(List<ChefGenerale> chefsGeneraux) {
        this.chefsGeneraux = ensureList(chefsGeneraux);
    }

    public List<ChefInformatique> getChefsInformatiques() {
        return ensureList(chefsInformatiques);
    }

    public void setChefsInformatiques(List<ChefInformatique> chefsInformatiques) {
        this.chefsInformatiques = ensureList(chefsInformatiques);
    }

    public List<Demande> getDemandes() {
        return ensureList(demandes);
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = ensureList(demandes);
    }

    public List<Notification> getNotifications() {
        return ensureList(notifications);
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = ensureList(notifications);
    }

    public List<Intervention> getInterventions() {
        return ensureList(interventions);
    }

    public void setInterventions(List<Intervention> interventions) {
        this.interventions = ensureList(interventions);
    }

    public List<RapportDeTravaille> getRapportsDeTravaille() {
        return ensureList(rapportsDeTravaille);
    }

    public void setRapportsDeTravaille(List<RapportDeTravaille> rapportsDeTravaille) {
        this.rapportsDeTravaille = ensureList(rapportsDeTravaille);
    }

    public List<Equipement> getEquipements() {
        return ensureList(equipements);
    }

    public void setEquipements(List<Equipement> equipements) {
        this.equipements = ensureList(equipements);
    }

    public List<EquipementIntervention> getEquipementInterventions() {
        return ensureList(equipementInterventions);
    }

    public void setEquipementInterventions(List<EquipementIntervention> equipementInterventions) {
        this.equipementInterventions = ensureList(equipementInterventions);
    }

    // endregion

    // region Utility helpers -----------------------------------------------------------------

    public void addCitoyen(Citoyen citoyen) {
        getCitoyens().add(citoyen);
    }

    public boolean removeCitoyenByCin(String cin) {
        return getCitoyens().removeIf(c -> cin.equals(c.getCin()));
    }

    public void addAgent(Agent agent) {
        getAgents().add(agent);
    }

    public boolean removeAgentByCin(String cin) {
        return getAgents().removeIf(a -> cin.equals(a.getCin()));
    }

    public void addResponsableMunicipalite(ResponsableMunicipalite responsableMunicipalite) {
        getResponsableMunicipalites().add(responsableMunicipalite);
    }

    public boolean removeResponsableMunicipaliteByCin(String cin) {
        return getResponsableMunicipalites().removeIf(s -> cin.equals(s.getCin()));
    }

    public void addChefGenerale(ChefGenerale chefGenerale) {
        getChefsGeneraux().add(chefGenerale);
    }

    public void addChefInformatique(ChefInformatique chefInformatique) {
        getChefsInformatiques().add(chefInformatique);
    }

    public void addDemande(Demande demande) {
        getDemandes().add(demande);
    }

    public boolean removeDemandeById(Integer identifiant) {
        return getDemandes().removeIf(d -> identifiant.equals(d.getIdentifiant()));
    }

    public void addNotification(Notification notification) {
        getNotifications().add(notification);
    }

    public boolean removeNotificationById(Integer id) {
        return getNotifications().removeIf(n -> id.equals(n.getIdN()));
    }

    public void addIntervention(Intervention intervention) {
        getInterventions().add(intervention);
    }

    public boolean removeInterventionById(Integer id) {
        return getInterventions().removeIf(i -> id.equals(i.getId()));
    }

    public void addRapport(RapportDeTravaille rapport) {
        getRapportsDeTravaille().add(rapport);
    }

    public boolean removeRapportByInterventionRef(Integer ref) {
        return getRapportsDeTravaille().removeIf(r -> ref.equals(r.getInterventionRef()));
    }

    public void addEquipement(Equipement equipement) {
        getEquipements().add(equipement);
    }

    public boolean removeEquipementById(Integer id) {
        return getEquipements().removeIf(e -> id.equals(e.getId()));
    }

    public void addEquipementIntervention(EquipementIntervention equipementIntervention) {
        getEquipementInterventions().add(equipementIntervention);
    }

    public boolean removeEquipementIntervention(Integer equipementRef, Integer interventionRef) {
        return getEquipementInterventions().removeIf(ei ->
                equipementRef.equals(ei.getEquipementRef()) && interventionRef.equals(ei.getInterventionRef()));
    }

    private static <T> List<T> ensureList(List<T> value) {
        return value == null ? new ArrayList<>() : value;
    }

    // endregion
}
