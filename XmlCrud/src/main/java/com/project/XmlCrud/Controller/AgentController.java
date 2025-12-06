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
    public ResponseEntity<AgentResponse> createAgent(@Valid @RequestBody AgentRequest request) {
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
        Agent agent = buildAgent(request);
        agent.setCin(cin);

        boolean updated = agentService.updateAgent(agent);
        if (!updated) {
            throw new NoSuchElementException("Agent introuvable");
        }
        return toResponse(agent);
    }

    @DeleteMapping("/{cin}")
    public ResponseEntity<Void> deleteAgent(@PathVariable String cin) {
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
