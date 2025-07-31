package com.example.classtouch360.db.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.classtouch360.db.entity.StudentGradesEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad NotaEstudiante.
 * Define las operaciones CRUD y de consulta para la tabla de notas de estudiantes.
 */
@Dao
public interface StudentGradesDao {
    /**
     * Inserta una nueva nota de estudiante. Si ya existe una nota con la misma
     * clave primaria (evaluationId, studentId), la reemplaza (upsert).
     * @param nota El objeto NotaEstudiante a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotaEstudiante(StudentGradesEntity nota);

    /**
     * Actualiza una nota de estudiante existente.
     * @param nota El objeto NotaEstudiante con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateNotaEstudiante(StudentGradesEntity nota);

    /**
     * Elimina una nota de estudiante.
     * @param nota El objeto NotaEstudiante a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteNotaEstudiante(StudentGradesEntity nota);

    /**
     * Obtiene todas las notas para una evaluación específica, ordenadas por ID de estudiante.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene una lista de objetos NotaEstudiante.
     */
    @Query("SELECT * FROM student_grades WHERE evaluationId = :evaluationId ORDER BY studentId ASC")
    LiveData<List<StudentGradesEntity>> getGradesForEvaluation(String evaluationId);

    /**
     * Obtiene una nota específica para un estudiante en una evaluación determinada.
     * @param evaluationId El ID de la evaluación.
     * @param studentId El ID del estudiante.
     * @return LiveData que contiene el objeto NotaEstudiante o null si no se encuentra.
     */
    @Query("SELECT * FROM student_grades WHERE evaluationId = :evaluationId AND studentId = :studentId LIMIT 1")
    LiveData<StudentGradesEntity> getGradeForStudentAndEvaluation(String evaluationId, String studentId);

    /**
     * Calcula el promedio de las notas de TODOS los estudiantes para una evaluación específica.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene el promedio (Double) o null si no hay notas.
     */
    @Query("SELECT AVG(grade) FROM student_grades WHERE evaluationId = :evaluationId")
    LiveData<Double> getAverageGradeForEvaluation(String evaluationId);

    /**
     * Obtiene la nota más alta para una evaluación específica.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene la nota más alta (Double) o null si no hay notas.
     */
    @Query("SELECT MAX(grade) FROM student_grades WHERE evaluationId = :evaluationId")
    LiveData<Double> getMaxGradeForEvaluation(String evaluationId);

    /**
     * Obtiene las cinco notas más altas para una evaluación específica, ordenadas de mayor a menor.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene una lista de los cinco puntajes más altos.
     */
    @Query("SELECT grade FROM student_grades WHERE evaluationId = :evaluationId ORDER BY grade DESC LIMIT 5")
    LiveData<List<Double>> getTop5GradesForEvaluation(String evaluationId);

    /**
     * Obtiene la nota más baja para una evaluación específica.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene la nota más baja (Double) o null si no hay notas.
     */
    @Query("SELECT MIN(grade) FROM student_grades WHERE evaluationId = :evaluationId")
    LiveData<Double> getLowestGradeForEvaluation(String evaluationId);

    /**
     * Obtiene las tres notas más bajas para una evaluación específica, ordenadas de menor a mayor.
     * @param evaluationId El ID de la evaluación.
     * @return LiveData que contiene una lista de los tres puntajes más bajos.
     */
    @Query("SELECT grade FROM student_grades WHERE evaluationId = :evaluationId ORDER BY grade ASC LIMIT 3")
    LiveData<List<Double>> getMin3GradesForEvaluation(String evaluationId); // Ya existía, lo mantengo.

    /**
     * Calcula el puntaje total acumulado para un estudiante en un parcial/semestre específico.
     * Une la tabla de notas con la de evaluaciones para filtrar por 'semester' y 'parcial'.
     * @param studentId El ID del estudiante.
     * @param semester El nombre del semestre.
     * @param parcial El nombre del parcial (puede ser null).
     * @return LiveData que contiene el puntaje total (Double) o null si no hay notas.
     */
    @Query("SELECT SUM(sg.grade) FROM student_grades sg " +
            "INNER JOIN evaluations e ON sg.evaluationId = e.evaluationId " +
            "WHERE sg.studentId = :studentId AND e.semester = :semester AND (:parcial IS NULL OR e.parcial = :parcial)")
    LiveData<Double> getTotalGradeForStudentAndSemesterAndParcial(String studentId, String semester, String parcial);
    /**
     * Obtiene una lista de notas de estudiantes que no han sido sincronizadas con la nube.
     * @return Una lista de objetos NotaEstudiante no sincronizados.
     */
    @Query("SELECT * FROM student_grades WHERE syncedToCloud = 0")
    List<StudentGradesEntity> getUnsyncedStudentGrades();
}
