package com.example.classtouch360.utils;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Clase modelo para representar un usuario en el sistema.
 * Incluye validaciones básicas y seguridad mejorada.
 */
public class HelperUser implements Serializable {
    private final String name;
    private final String email;
    private final String username;
    private transient String password;


    public HelperUser() {
        this.name = "";
        this.email = "";
        this.username = "";
        this.password = "";
    }

    // Constructor alternativo para login con Google
    public HelperUser(@NonNull String name, @NonNull String email, @NonNull String username) {
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.username = username.trim().toLowerCase();
        this.password = ""; // No se usa
    }


    // Constructor principal
    public HelperUser(@NonNull String name,
                      @NonNull String email,
                      @NonNull String username,
                      @NonNull String password) {

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Ningún campo puede estar vacío");
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.username = username.trim().toLowerCase();
        this.password = password; // En producción, debería ser un hash
    }

    // Getters (sin setters para inmutabilidad)
    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    // Precaución: Solo para uso interno
    @NonNull
    protected String getPassword() {
        return password;
    }

    // Método para validar credenciales
    public boolean validatePassword(@NonNull String inputPassword) {
        return this.password.equals(inputPassword);
        // En producción: return hashPassword(inputPassword).equals(this.password);
    }

    // Método toString() para logging
    @Override
    @NonNull
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}'; // Password omitido por seguridad
    }

    // Ejemplo de método para hashing (deberías usar BCrypt en producción)
    public static String hashPassword(@NonNull String plainText) {
        // Implementación real debería usar BCrypt o Argon2
        return Integer.toString(plainText.hashCode());
    }
}