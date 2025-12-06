package com.project.XmlCrud.Controller;

import com.project.XmlCrud.DTO.CitoyenRequest;
import com.project.XmlCrud.DTO.LoginRequest;
import com.project.XmlCrud.Model.Citoyen;
import com.project.XmlCrud.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        return Map.of("token", token);
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@Valid @RequestBody CitoyenRequest request) {
        Citoyen citoyen = new Citoyen(
                request.getCin(),
                request.getEmail(),
                request.getPassword(),
                request.getNom(),
                request.getPrenom(),
                "citoyen",
                request.getAdresse(),
                request.getRue()
        );
        String token = authService.registerCitoyen(citoyen);
        return Map.of("token", token);
    }
}
