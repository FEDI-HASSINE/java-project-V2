package com.project.XmlCrud.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RapportRequest(
        @NotNull @Min(0) Integer cout,
        @NotNull @Min(1) Integer duree,
        @NotBlank String description,
        String imageBase64
) {
}
