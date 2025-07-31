package com.example.classtouch360.db.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.classtouch360.db.entity.StudentEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para consultas estadísticas y complejas
 * que involucran múltiples tablas como Evaluacion, NotaEstudiante y Estudiante.
 */
@Dao
public interface StadisticsDao {

    /**
     * Calcula el promedio de notas general para una clase específica,
     * considerando todas las evaluaciones de un semestre y parcial dados.
     * @param classId El ID de la clase.
     * @param semester El nombre del semestre.
     * @param parcial El nombre del parcial (puede ser null).
     * @return LiveData que contiene el promedio general de la clase para ese periodo.
     */
    @Query("SELECT AVG(sg.grade) " +
            "FROM student_grades sg " +
            "INNER JOIN evaluations e ON sg.evaluationId = e.evaluationId " +
            "WHERE e.classId = :classId AND e.semester = :semester AND (:parcial IS NULL OR e.parcial = :parcial)")
    LiveData<Double> getClassAverageGradeForSemesterAndParcial(String classId, String semester, String parcial);

    /**
     * Obtiene los 3 estudiantes con el mejor promedio de notas para una clase, semestre y parcial dados.
     * Retorna LiveData que contiene una lista de objetos Estudiante.
     * @param classId El ID de la clase.
     * @param semester El nombre del semestre.
     * @param parcial El nombre del parcial (puede ser null).
     * @return LiveData que contiene una lista de los 3 estudiantes con mejor promedio.
     */
    @Query("SELECT s.*, AVG(sg.grade) AS averageGrade " +
            "FROM students s " +
            "INNER JOIN student_grades sg ON s.studentId = sg.studentId " +
            "INNER JOIN evaluations e ON sg.evaluationId = e.evaluationId " +
            "WHERE s.classId = :classId AND e.semester = :semester AND (:parcial IS NULL OR e.parcial = :parcial) " +
            "GROUP BY s.studentId " +
            "ORDER BY averageGrade DESC " +
            "LIMIT 3")
    LiveData<List<StudentEntity>> getTop3StudentsByAverageGradeForSemesterAndParcial(String classId, String semester, String parcial);

    /**
     * Obtiene los 3 estudiantes con el peor promedio de notas para una clase, semestre y parcial dados.
     * Retorna LiveData que contiene una lista de objetos Estudiante.
     * @param classId El ID de la clase.
     * @param semester El nombre del semestre.
     * @param parcial El nombre del parcial (puede ser null).
     * @return LiveData que contiene una lista de los 3 estudiantes con peor promedio.
     */
    @Query("SELECT s.*, AVG(sg.grade) AS averageGrade " +
            "FROM students s " +
            "INNER JOIN student_grades sg ON s.studentId = sg.studentId " +
            "INNER JOIN evaluations e ON sg.evaluationId = e.evaluationId " +
            "WHERE s.classId = :classId AND e.semester = :semester AND (:parcial IS NULL OR e.parcial = :parcial) " +
            "GROUP BY s.studentId " +
            "ORDER BY averageGrade ASC " +
            "LIMIT 3")
    LiveData<List<StudentEntity>> getBottom3StudentsByAverageGradeForSemesterAndParcial(String classId, String semester, String parcial);
}