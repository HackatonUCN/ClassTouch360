package com.example.classtouch360.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Entidad que representa la nota obtenida por un estudiante en una evaluación específica.
 * Utiliza una clave primaria compuesta por evaluationId y studentId para asegurar la unicidad.
 */
@Entity(
        tableName = "student_grades",
        // Define una clave primaria compuesta por evaluationId y studentId.
        // Esto asegura que solo puede haber una nota por estudiante por evaluación.
        primaryKeys = {"evaluationId", "studentId"},
        // Define claves foráneas para vincular la nota a una evaluación y a un estudiante.
        // Si una evaluación o un estudiante se eliminan, sus notas asociadas también se eliminan (CASCADE).
        foreignKeys = {
                @ForeignKey(
                        entity = EvaluationEntity.class,
                        parentColumns = "evaluationId",
                        childColumns = "evaluationId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = StudentEntity.class,
                        parentColumns = "studentId",
                        childColumns = "studentId",
                        onDelete = CASCADE
                )
        },
        // Crea índices para optimizar las búsquedas por evaluación y por estudiante.
        indices = {@Index(value = {"evaluationId"}), @Index(value = {"studentId"})}
)
public class StudentGradesEntity {
    // Clave foránea que referencia el ID de la evaluación.
    @NonNull
    private String evaluationId;

    // Clave foránea que referencia el ID del estudiante.
    @NonNull
    private String studentId;

    // Puntaje obtenido por el estudiante en esta evaluación.
    private double grade;

    // Observaciones o comentarios sobre la nota del estudiante (opcional).
    @Nullable
    private String observation;

    // Timestamp de la última modificación de este registro.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la base de datos en la nube (Firebase).
    private boolean syncedToCloud;

    // Constructor que Room utilizará para reconstruir objetos desde la base de datos.
    public StudentGradesEntity(@NonNull String evaluationId, @NonNull String studentId, double grade, @Nullable String observation, long lastModified, boolean syncedToCloud) {
        this.evaluationId = evaluationId;
        this.studentId = studentId;
        this.grade = grade;
        this.observation = observation;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }

    @Ignore
    // Constructor auxiliar para crear una nueva nota con timestamps por defecto.
    public StudentGradesEntity(@NonNull String evaluationId, @NonNull String studentId, double grade, @Nullable String observation) {
        this(evaluationId, studentId, grade, observation, System.currentTimeMillis(), false);
    }

    // --- Getters ---
    @NonNull
    public String getEvaluationId() {
        return evaluationId;
    }

    @NonNull
    public String getStudentId() {
        return studentId;
    }

    public double getGrade() {
        return grade;
    }

    @Nullable
    public String getObservation() {
        return observation;
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

    public void setStudentId(@NonNull String studentId) {
        this.studentId = studentId;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setObservation(@Nullable String observation) {
        this.observation = observation;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSyncedToCloud(boolean syncedToCloud) {
        this.syncedToCloud = syncedToCloud;
    }
}