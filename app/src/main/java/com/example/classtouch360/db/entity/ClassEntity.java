package com.example.classtouch360.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Entidad que representa una clase o materia en la base de datos local.
 * Cada clase pertenece a un usuario específico.
 */
@Entity(
        tableName = "classes",
        // Define una clave foránea que vincula cada clase a un usuario.
        // Si se elimina un usuario, todas sus clases asociadas también se eliminarán (CASCADE).
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "userId",
                childColumns = "userId",
                onDelete = CASCADE
        ),
        // Crea un índice en 'userId' para optimizar las búsquedas de clases por usuario.
        indices = @Index(value = {"userId"})
)
public class ClassEntity {
    // Clave primaria de la clase. Se genera un UUID único por defecto.
    @PrimaryKey
    @NonNull
    private String classId;

    // Clave foránea que referencia el userId de la tabla 'users'.
    @NonNull
    private String userId;

    // Nombre de la sección o aula (ej., "5to Grado A", "Matemáticas Avanzadas").
    @NonNull
    private String className;

    // Nombre de la materia que se imparte en esta clase.
    @NonNull
    private String subjectName;

    // Timestamp de cuando se creó este registro.
    private long creationDate;

    // Timestamp de la última modificación de este registro.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la nube.
    private boolean syncedToCloud;

    // Constructor para Room.
    public ClassEntity(@NonNull String classId, @NonNull String userId, @NonNull String className, @NonNull String subjectName, long creationDate, long lastModified, boolean syncedToCloud) {
        this.classId = classId;
        this.userId = userId;
        this.className = className;
        this.subjectName = subjectName;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }
    @Ignore
    // Constructor auxiliar para crear una nueva clase con UUID y timestamps por defecto.
    public ClassEntity(@NonNull String userId, @NonNull String className, @NonNull String subjectName) {
        this(UUID.randomUUID().toString(), userId, className, subjectName, System.currentTimeMillis(), System.currentTimeMillis(), false);
    }

    // Getters
    @NonNull
    public String getClassId() {
        return classId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getClassName() {
        return className;
    }

    @NonNull
    public String getSubjectName() {
        return subjectName;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isSyncedToCloud() {
        return syncedToCloud;
    }

    // Setters
    public void setClassId(@NonNull String classId) {
        this.classId = classId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public void setClassName(@NonNull String className) {
        this.className = className;
    }

    public void setSubjectName(@NonNull String subjectName) {
        this.subjectName = subjectName;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSyncedToCloud(boolean syncedToCloud) {
        this.syncedToCloud = syncedToCloud;
    }
}
