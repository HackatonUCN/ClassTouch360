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
 * Entidad que representa una evaluación (tarea, examen, proyecto) en la base de datos local.
 * Cada evaluación está asociada a una clase específica.
 */
@Entity(
        tableName = "evaluations",
        foreignKeys = @ForeignKey(
                entity = ClassEntity.class,
                parentColumns = "classId",
                childColumns = "classId",
                onDelete = CASCADE
        ),
        indices = @Index(value = {"classId"})
)
public class EvaluationEntity {
    // Clave primaria de la evaluación. Se genera un UUID único por defecto.
    @PrimaryKey
    @NonNull
    private String evaluationId;

    // Clave foránea que referencia el classId de la tabla 'classes'.
    @NonNull
    private String classId;

    // Nombre de la evaluación (ej., "Examen Parcial 1", "Tarea #3", "Proyecto Final").
    @NonNull
    private String evaluationName;

    // Descripción detallada de la evaluación (opcional).
    @Nullable
    private String description;

    // Puntaje máximo que se puede obtener en esta evaluación.
    private double maxScore;

    // Fecha de creación de la evaluación (timestamp en milisegundos).
    private long creationDate;

    // Fecha de entrega o finalización de la evaluación (timestamp en milisegundos).
    private long dueDate;

    // Semestre al que corresponde la evaluación (ej., "Primer Semestre", "Segundo Semestre").
    @NonNull
    private String semester;

    // Parcial específico dentro del semestre (ej., "Primer Parcial", "Segundo Parcial", "Final").
    // Puede ser nulo si la evaluación no corresponde a un parcial específico o es un examen final de semestre.
    @Nullable
    private String parcial;

    // Campo para guardar la URL o ruta local de un documento o imagen asociado a la evaluación.
    // Los archivos grandes (imágenes, PDFs) NO deben guardarse directamente en SQLite.
    // Se recomienda usar Firebase Storage para los archivos y guardar aquí solo la referencia (URL).
    @Nullable
    private String documentRef; // Descomentar si se implementa esta funcionalidad.

    // Timestamp de la última modificación de este registro.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la nube.
    private boolean syncedToCloud;

    // Constructor para Room.
    public EvaluationEntity(@NonNull String evaluationId, @NonNull String classId, @NonNull String evaluationName, @Nullable String description, double maxScore, long creationDate, long dueDate, @NonNull String semester, @Nullable String parcial, @Nullable String documentRef, long lastModified, boolean syncedToCloud) {
        this.evaluationId = evaluationId;
        this.classId = classId;
        this.evaluationName = evaluationName;
        this.description = description;
        this.maxScore = maxScore;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.semester = semester;
        this.parcial = parcial;
        this.documentRef = documentRef;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }

    @Ignore
    // Constructor auxiliar para crear una nueva evaluación con UUID y timestamps por defecto.
    public EvaluationEntity(@NonNull String classId, @NonNull String evaluationName, @Nullable String description, double maxScore, long creationDate, long dueDate, @NonNull String semester, @Nullable String parcial) {
        this(UUID.randomUUID().toString(), classId, evaluationName, description, maxScore, creationDate, dueDate, semester, parcial, null, System.currentTimeMillis(), false);
    }

    // --- Getters ---
    @NonNull
    public String getEvaluationId() {
        return evaluationId;
    }

    @NonNull
    public String getClassId() {
        return classId;
    }

    @NonNull
    public String getEvaluationName() {
        return evaluationName;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    @NonNull
    public String getSemester() {
        return semester;
    }

    @Nullable
    public String getParcial() {
        return parcial;
    }

    @Nullable
    public String getDocumentRef() {
        return documentRef;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isSyncedToCloud() {
        return syncedToCloud;
    }

    // --- Setters ---
    public void setEvaluationId(@NonNull String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public void setClassId(@NonNull String classId) {
        this.classId = classId;
    }

    public void setEvaluationName(@NonNull String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void setSemester(@NonNull String semester) {
        this.semester = semester;
    }

    public void setParcial(@Nullable String parcial) {
        this.parcial = parcial;
    }

    public void setDocumentRef(@Nullable String documentRef) {
        this.documentRef = documentRef;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSyncedToCloud(boolean syncedToCloud) {
        this.syncedToCloud = syncedToCloud;
    }
}
