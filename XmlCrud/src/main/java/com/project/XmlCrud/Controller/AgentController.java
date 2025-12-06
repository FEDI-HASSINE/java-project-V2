package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.AgentRequest;
import com.project.XmlCrud.DTO.AgentResponse;
import com.project.XmlCrud.Model.Agent;
import com.project.XmlCrud.Service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/agents")
public class AgentController {

    private static final String ROLE_AGENT = "agent";

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ResponseEntity<?> createAgent(@Valid @RequestBody AgentRequest request) {
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Le mot de passe doit contenir au moins 6 caractères");
        }
        if (request.getDisponibilite() == null) {
             return ResponseEntity.badRequest().body("La disponibilité est requise pour la création");
        }
        Agent agent = buildAgent(request);
        Agent created = agentService.addAgent(agent);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    public List<AgentResponse> getAllAgents() {
        return agentService.getAllAgents()
                .stream()
                .map(AgentController::toResponse)
                .toList();
    }

    @GetMapping("/{cin}")
    public AgentResponse getAgent(@PathVariable String cin) {
        Agent agent = agentService.getAgentByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("Agent introuvable"));
        return toResponse(agent);
    }

    @PutMapping("/{cin}")
    public AgentResponse updateAgent(@PathVariable String cin, @Valid @RequestBody AgentRequest request) {
        Agent existing = agentService.getAgentByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("Agent introuvable"));

        String passwordToUse = request.getPassword();
        if (passwordToUse == null || passwordToUse.isEmpty()) {
            passwordToUse = existing.getPassword();
        } else {
            if (passwordToUse.length() < 6) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
            }
        }

        Boolean dispoToUse = request.getDisponibilite();
        if (dispoToUse == null) {
            dispoToUse = existing.isDisponibilite();
        }

        Agent agent = new Agent(
                cin,
                request.getEmail().trim().toLowerCase(),
                passwordToUse,
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_AGENT,
                dispoToUse,
                request.getService().trim()
        );

        boolean updated = agentService.updateAgent(agent);
        if (!updated) {
            throw new NoSuchElementException("Agent introuvable");
        }
        return toResponse(agent);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<?> deleteAgent(@PathVariable String cin) {
        Agent existing = agentService.getAgentByCIN(cin)
                .orElseThrow(() -> new NoSuchElementException("Agent introuvable"));
        
        if (!existing.isDisponibilite()) {
            return ResponseEntity.badRequest().body("Impossible de supprimer un agent non disponible");
        }

        boolean removed = agentService.deleteAgent(cin);
        if (!removed) {
            throw new NoSuchElementException("Agent introuvable");
        }
        return ResponseEntity.noContent().build();
    }

    private static Agent buildAgent(AgentRequest request) {
        return new Agent(
                request.getCin().trim(),
                request.getEmail().trim().toLowerCase(),
                request.getPassword(),
                request.getNom().trim(),
                request.getPrenom().trim(),
                ROLE_AGENT,
                request.getDisponibilite(),
                request.getService().trim()
        );
    }

    private static AgentResponse toResponse(Agent agent) {
        return new AgentResponse(
                agent.getCin(),
                agent.getEmail(),
                agent.getNom(),
                agent.getPrenom(),
                agent.isDisponibilite(),
                agent.getService(),
                agent.getRole()
        );
    }
}
