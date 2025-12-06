package com.project.XmlCrud.Model.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * JAXB adapter to marshal/unmarshal {@link LocalDate} using ISO-8601 format (yyyy-MM-dd).
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

    @Override
    public LocalDate unmarshal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Date invalide: " + value, ex);
        }
    }

    @Override
    public String marshal(LocalDate value) {
        return value == null ? null : FORMATTER.format(value);
    }
}
