package com.example.classtouch360.db.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Entidad que representa a un estudiante en la base de datos local.
 * Cada estudiante está asociado a una clase específica.
 */
@Entity(
        tableName = "students",
        // Define una clave foránea que vincula cada estudiante a una clase.
        // Si se elimina una clase, todos sus estudiantes asociados también se eliminarán (CASCADE).
        foreignKeys = @ForeignKey(
                entity = ClassEntity.class,
                parentColumns = "classId",
                childColumns = "classId",
                onDelete = CASCADE
        ),
        // Crea un índice en 'classId' para optimizar las búsquedas de estudiantes por clase.
        indices = @Index(value = {"classId"})
)
public class StudentEntity {
    // Clave primaria del estudiante. Se genera un UUID único por defecto.
    @PrimaryKey
    @NonNull
    private String studentId;

    // Clave foránea que referencia el classId de la tabla 'classes'.
    @NonNull
    private String classId;

    // Número de lista del estudiante (puede ser nulo).
    @Nullable // Indica que este campo puede ser nulo en la base de datos
    private Integer studentNumber; // Usar Integer para que pueda ser null

    // Nombre del estudiante.
    @NonNull
    private String firstName;

    // Código estudiantil del estudiante (puede ser nulo).
    @Nullable // Indica que este campo puede ser nulo en la base de datos
    private String studentCode;

    // Timestamp de cuando se creó este registro.
    private long creationDate;

    // Timestamp de la última modificación de este registro.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la nube.
    private boolean syncedToCloud;

    // Constructor para Room.
    public StudentEntity(@NonNull String studentId, @NonNull String classId, @Nullable Integer studentNumber, @NonNull String firstName, @Nullable String studentCode, long creationDate, long lastModified, boolean syncedToCloud) {
        this.studentId = studentId;
        this.classId = classId;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.studentCode = studentCode;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }

    @Ignore
    // Constructor auxiliar para crear un nuevo estudiante con UUID y timestamps por defecto.
    public StudentEntity(@NonNull String classId, @Nullable Integer studentNumber, @NonNull String firstName, @Nullable String studentCode) {
        this(UUID.randomUUID().toString(), classId, studentNumber, firstName, studentCode, System.currentTimeMillis(), System.currentTimeMillis(), false);
    }

    // Getters
    @NonNull
    public String getStudentId() {
        return studentId;
    }

    @NonNull
    public String getClassId() {
        return classId;
    }

    @Nullable
    public Integer getStudentNumber() {
        return studentNumber;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getStudentCode() {
        return studentCode;
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
    public void setStudentId(@NonNull String studentId) {
        this.studentId = studentId;
    }

    public void setClassId(@NonNull String classId) {
        this.classId = classId;
    }

    public void setStudentNumber(@Nullable Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setStudentCode(@Nullable String studentCode) {
        this.studentCode = studentCode;
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