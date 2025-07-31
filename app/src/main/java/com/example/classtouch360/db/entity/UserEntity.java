package com.example.classtouch360.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa a un usuario (docente) en la base de datos local.
 * Su ID principal es el mismo que el UID proporcionado por Firebase Authentication.
 */
@Entity(tableName = "users")
public class UserEntity {
    // Clave primaria, debe coincidir con el UID de Firebase para facilitar la sincronización.
    // @NonNull indica que este campo no puede ser nulo.
    @PrimaryKey
    @NonNull
    private String userId;

    // Nombre completo del usuario.
    @NonNull
    private String name;

    // Dirección de correo electrónico del usuario.
    @NonNull
    private String email;

    // Nombre de usuario único (si se requiere uno distinto del email).
    @NonNull
    private String username;

    // Timestamp de la última modificación de este registro (en milisegundos desde la época).
    // Útil para la resolución de conflictos en la sincronización.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la base de datos en la nube (Firebase).
    // 'false' significa que hay cambios locales pendientes de subir.
    private boolean syncedToCloud;

    // Constructor para Room. Room utiliza los setters para asignar valores.
    // Es buena práctica tener un constructor que Room pueda usar.
    public UserEntity(@NonNull String userId, @NonNull String name, @NonNull String email, @NonNull String username, long lastModified, boolean syncedToCloud) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }

    // Constructor auxiliar para crear un nuevo usuario con valores por defecto para lastModified y syncedToCloud.
    @Ignore
    public UserEntity(@NonNull String userId, @NonNull String name, @NonNull String email, @NonNull String username) {
        this(userId, name, email, username, System.currentTimeMillis(), false);
    }

    // Getters para todos los campos. Room los necesita para leer los datos.
    @NonNull
    public String getUserId() {
        return userId;
    }

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

    public long getLastModified() {
        return lastModified;
    }

    public boolean isSyncedToCloud() {
        return syncedToCloud;
    }

    // Setters para los campos que pueden ser modificados. Room los usa para escribir datos.
    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSyncedToCloud(boolean syncedToCloud) {
        this.syncedToCloud = syncedToCloud;
    }
}