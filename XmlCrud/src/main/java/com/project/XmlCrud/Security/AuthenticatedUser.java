package com.project.XmlCrud.Security;

public class AuthenticatedUser {

    private final String cin;
    private final String email;
    private final String role;

    public AuthenticatedUser(String cin, String email, String role) {
        this.cin = cin;
        this.email = email;
        this.role = role;
    }

    public String getCin() {
        return cin;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return email;
    }
}
