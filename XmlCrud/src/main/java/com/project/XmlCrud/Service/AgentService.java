package com.project.XmlCrud.Service;

import com.project.XmlCrud.Model.Agent;
import com.project.XmlCrud.Model.Municipalite;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    public Agent addAgent(Agent agent) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean exists = municipalite.getAgents().stream()
                .anyMatch(existing -> existing.getCin().equals(agent.getCin())
                        || existing.getEmail().equalsIgnoreCase(agent.getEmail()));
        if (exists) {
            throw new IllegalArgumentException("Agent avec ce CIN ou email existe déjà");
        }

        municipalite.addAgent(agent);
        XmlUtil.saveMunicipalite(municipalite);
        return agent;
    }

    public List<Agent> getAllAgents() {
        return XmlUtil.loadMunicipalite().getAgents();
    }

    public Optional<Agent> getAgentByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String normalized = email.trim().toLowerCase();
        return XmlUtil.loadMunicipalite().getAgents()
                .stream()
                .filter(a -> a.getEmail().equalsIgnoreCase(normalized))
                .findFirst();
    }

    public Optional<Agent> getAgentByCIN(String cin) {
        return XmlUtil.loadMunicipalite().getAgents()
                .stream()
                .filter(a -> a.getCin().equals(cin))
                .findFirst();
    }

    public boolean updateAgent(Agent updatedAgent) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();

        boolean emailTaken = municipalite.getAgents().stream()
                .anyMatch(existing -> !existing.getCin().equals(updatedAgent.getCin())
                        && existing.getEmail().equalsIgnoreCase(updatedAgent.getEmail()));
        if (emailTaken) {
            throw new IllegalArgumentException("Adresse email déjà utilisée par un autre agent");
        }

        for (int i = 0; i < municipalite.getAgents().size(); i++) {
            Agent existing = municipalite.getAgents().get(i);
            if (existing.getCin().equals(updatedAgent.getCin())) {
                municipalite.getAgents().set(i, updatedAgent);
                XmlUtil.saveMunicipalite(municipalite);
                return true;
            }
        }
        return false;
    }

    public boolean deleteAgent(String cin) {
        Municipalite municipalite = XmlUtil.loadMunicipalite();
        boolean removed = municipalite.removeAgentByCin(cin);
        if (removed) {
            XmlUtil.saveMunicipalite(municipalite);
        }
        return removed;
    }
}
