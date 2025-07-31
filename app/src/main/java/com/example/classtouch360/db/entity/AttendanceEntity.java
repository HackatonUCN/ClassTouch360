package com.example.classtouch360.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Entidad que representa un registro de asistencia para un estudiante en una clase y fecha específicas.
 * Utiliza una clave primaria compuesta por studentId, classId y date para asegurar la unicidad.
 */
@Entity(
        tableName = "attendance",
        // Define una clave primaria compuesta por studentId, classId y date.
        // Esto asegura que solo puede haber un registro de asistencia por estudiante, clase y día.
        primaryKeys = {"studentId", "classId", "date"},
        // Define claves foráneas para vincular la asistencia a un estudiante y a una clase.
        // Si un estudiante o una clase se eliminan, sus registros de asistencia asociados también se eliminan (CASCADE).
        foreignKeys = {
                @ForeignKey(
                        entity = StudentEntity.class,
                        parentColumns = "studentId",
                        childColumns = "studentId",
                        onDelete = CASCADE
                ),
                @ForeignKey(
                        entity = ClassEntity.class,
                        parentColumns = "classId",
                        childColumns = "classId",
                        onDelete = CASCADE
                )


        },
        indices = {
                @Index(value = {"studentId", "classId", "date"}),
                @Index(value = {"classId"})
        }
)
public class AttendanceEntity {
    // Clave foránea que referencia el ID del estudiante.
    @NonNull
    private String studentId;

    // Clave foránea que referencia el ID de la clase.
    @NonNull
    private String classId;

    // Fecha de la asistencia en formato timestamp (milisegundos desde la época).
    // Parte de la clave primaria compuesta.
    private long date;

    // Estado de la asistencia: 'P' (Presente), 'A' (Ausente), 'J' (Justificado).
    @NonNull
    private String status;

    // Timestamp de la última modificación de este registro.
    // Útil para la resolución de conflictos en la sincronización.
    private long lastModified;

    // Booleano que indica si este registro ha sido sincronizado con la base de datos en la nube (Firebase).
    // 'false' significa que hay cambios locales pendientes de subir.
    private boolean syncedToCloud;

    // Constructor que Room utilizará para reconstruir objetos desde la base de datos.
    public AttendanceEntity(@NonNull String studentId, @NonNull String classId, long date, @NonNull String status, long lastModified, boolean syncedToCloud) {
        this.studentId = studentId;
        this.classId = classId;
        this.date = date;
        this.status = status;
        this.lastModified = lastModified;
        this.syncedToCloud = syncedToCloud;
    }

    // Constructor auxiliar para crear un nuevo registro de asistencia con valores por defecto
    // para lastModified y syncedToCloud.
    @Ignore
    public AttendanceEntity(@NonNull String studentId, @NonNull String classId, long date, @NonNull String status) {
        this(studentId, classId, date, status, System.currentTimeMillis(), false);
    }

    // --- Getters ---
    @NonNull
    public String getStudentId() {
        return studentId;
    }

    @NonNull
    public String getClassId() {
        return classId;
    }

    public long getDate() {
        return date;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isSyncedToCloud() {
        return syncedToCloud;
    }

    // --- Setters ---
    public void setStudentId(@NonNull String studentId) {
        this.studentId = studentId;
    }

    public void setClassId(@NonNull String classId) {
        this.classId = classId;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setSyncedToCloud(boolean syncedToCloud) {
        this.syncedToCloud = syncedToCloud;
    }
}