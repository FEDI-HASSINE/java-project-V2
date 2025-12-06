package com.project.XmlCrud.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/demandes/create").hasAuthority("citoyen")
                .requestMatchers(HttpMethod.POST, "/demandes/*/interventions").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.POST, "/demandes/*/notifications").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.POST, "/equipements").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.GET, "/equipements").hasAnyAuthority("responsableMunicipalite", "chef")
                .requestMatchers(HttpMethod.GET, "/interventions/*/equipements").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.POST, "/interventions/*/equipements").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.POST, "/interventions/*/update").hasAuthority("responsableMunicipalite")
                .requestMatchers(HttpMethod.DELETE, "/notifications/*").hasAuthority("citoyen")
                .requestMatchers(HttpMethod.POST, "/interventions/*/rapports").hasAuthority("agent")
                .requestMatchers(HttpMethod.PATCH, "/interventions/*/etat").hasAuthority("agent")
                .requestMatchers(HttpMethod.GET, "/rapports").hasAuthority("chef")
                .requestMatchers(HttpMethod.PUT, "/chefs-generaux/*").hasAuthority("chefinfo")
                .requestMatchers(HttpMethod.GET, "/chefs-generaux").hasAuthority("chefinfo")
                .requestMatchers(HttpMethod.GET, "/agents").hasAnyAuthority("chefinfo", "responsableMunicipalite", "chef")
                .requestMatchers("/agents/**").hasAuthority("chefinfo")
                .requestMatchers("/chefs-informatiques/**").hasAuthority("chefinfo")
                .requestMatchers("/citoyens/**").hasAnyAuthority("chefinfo", "chef")
                .requestMatchers("/responsableMunicipalites/**").hasAuthority("chefinfo")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Autoriser toutes les origines via pattern
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
