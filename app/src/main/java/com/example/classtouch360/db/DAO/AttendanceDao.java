package com.example.classtouch360.db.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.classtouch360.db.entity.AttendanceEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Asistencia.
 * Define las operaciones CRUD y de consulta para la tabla de asistencia.
 */
@Dao
public interface AttendanceDao {
    /**
     * Inserta un nuevo registro de asistencia. Si ya existe un registro con la misma
     * clave primaria (studentId, classId, date), lo reemplaza (upsert).
     * Esto es útil para la lógica de guardar/actualizar asistencia.
     * @param asistencia El objeto Asistencia a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAsistencia(AttendanceEntity asistencia);

    /**
     * Actualiza un registro de asistencia existente.
     * @param asistencia El objeto Asistencia con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateAsistencia(AttendanceEntity asistencia);

    /**
     * Elimina un registro de asistencia.
     * @param asistencia El objeto Asistencia a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteAsistencia(AttendanceEntity asistencia);

    /**
     * Obtiene el estado de asistencia (solo el String 'status') para un estudiante y fecha específicos.
     * Esta es la traducción de tu método `getStatus(long sid, String date)`.
     * Se asume que 'date' en la entidad Asistencia es un timestamp (long).
     * @param studentId El ID del estudiante.
     * @param date La fecha de la asistencia (timestamp).
     * @return LiveData que contiene el String del estado ('P', 'A', 'J') o null si no se encuentra.
     */
    @Query("SELECT status FROM attendance WHERE studentId = :studentId AND date = :date LIMIT 1")
    LiveData<String> getStatusForStudentAndDate(String studentId, long date);

    /**
     * Obtiene el estado de asistencia (solo el String 'status') para un estudiante, clase y fecha específicos.
     * Esta es la traducción de tu método `getStatus1(long sid, long cid, String date)`.
     * Se asume que 'date' en la entidad Asistencia es un timestamp (long).
     * @param studentId El ID del estudiante.
     * @param classId El ID de la clase.
     * @param date La fecha de la asistencia (timestamp).
     * @return LiveData que contiene el String del estado ('P', 'A', 'J') o null si no se encuentra.
     */
    @Query("SELECT status FROM attendance WHERE studentId = :studentId AND classId = :classId AND date = :date LIMIT 1")
    LiveData<String> getStatusForStudentClassAndDate(String studentId, String classId, long date);

    /**
     * Obtiene todos los registros de asistencia para una clase y fecha específicas.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param classId El ID de la clase.
     * @param date La fecha de la asistencia (timestamp).
     * @return LiveData que contiene una lista de objetos Asistencia.
     */
    @Query("SELECT * FROM attendance WHERE classId = :classId AND date = :date ORDER BY studentId ASC")
    LiveData<List<AttendanceEntity>> getAttendanceForClassAndDate(String classId, long date);

    /**
     * Obtiene el historial completo de asistencia para un estudiante específico, ordenado por fecha descendente.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param studentId El ID del estudiante.
     * @return LiveData que contiene una lista de objetos Asistencia.
     */
    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    LiveData<List<AttendanceEntity>> getAttendanceHistoryForStudent(String studentId);

    /**
     * Obtiene una lista de registros de asistencia que no han sido sincronizados con la nube.
     * @return Una lista de objetos Asistencia no sincronizados.
     */
    @Query("SELECT * FROM attendance WHERE syncedToCloud = 0")
    List<AttendanceEntity> getUnsyncedAttendance();

    /**
     * Obtiene los meses y años distintos con registros de asistencia para una clase específica.
     * Esta es la traducción de tu método `getDistinctMonths(long cid)`.
     * Se utiliza la función `strftime` de SQLite para formatear el timestamp a 'YYYY-MM'.
     * @param classId El ID de la clase.
     * @return LiveData que contiene una lista de Strings en formato "YYYY-MM" (ej., "2024-07").
     */
    @Query("SELECT DISTINCT strftime('%Y-%m', date / 1000, 'unixepoch') FROM attendance WHERE classId = :classId ORDER BY date DESC")
    LiveData<List<String>> getDistinctMonthsForClass(String classId);

    /**
     * Obtiene los meses y años distintos con registros de asistencia para un estudiante específico.
     * Útil para el historial de asistencia individual.
     * @param studentId El ID del estudiante.
     * @return LiveData que contiene una lista de Strings en formato "YYYY-MM".
     */
    @Query("SELECT DISTINCT strftime('%Y-%m', date / 1000, 'unixepoch') FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    LiveData<List<String>> getDistinctMonthsForStudent(String studentId);
}