package com.example.classtouch360.db.DAO;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classtouch360.db.entity.EvaluationEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Evaluacion.
 * Define las operaciones CRUD y de consulta para la tabla de evaluaciones.
 */
@Dao
public interface EvaluationDao {
    /**
     * Inserta una nueva evaluación. Si ya existe una evaluación con el mismo ID, la reemplaza (upsert).
     * @param evaluacion El objeto Evaluacion a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvaluacion(EvaluationEntity evaluacion);

    /**
     * Actualiza una evaluación existente.
     * @param evaluacion El objeto Evaluacion con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateEvaluacion(EvaluationEntity evaluacion);

    /**
     * Elimina una evaluación.
     * @param evaluacion El objeto Evaluacion a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteEvaluacion(EvaluationEntity evaluacion);

    /**
     * Obtiene todas las evaluaciones asociadas a una clase específica, ordenadas por fecha de entrega descendente.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param classId El ID de la clase.
     * @return LiveData que contiene una lista de objetos Evaluacion.
     */
    @Query("SELECT * FROM evaluations WHERE classId = :classId ORDER BY dueDate DESC")
    LiveData<List<EvaluationEntity>> getEvaluationsForClass(String classId);

    /**
     * Obtiene una evaluación por su ID.
     * @param evaluationId El ID de la evaluación.
     * @return El objeto Evaluacion o null si no se encuentra.
     */
    @Query("SELECT * FROM evaluations WHERE evaluationId = :evaluationId")
    EvaluationEntity getEvaluacionById(String evaluationId);

    /**
     * Obtiene una evaluación por su nombre.
     * @param evaluationName El nombre de la evaluación a buscar.
     * @return LiveData que contiene una lista de objetos Evaluacion que coinciden con el nombre.
     */
    @Query("SELECT * FROM evaluations WHERE evaluationName LIKE :evaluationName ORDER BY dueDate DESC")
    LiveData<List<EvaluationEntity>> getEvaluationsByName(String evaluationName);

    /**
     * Obtiene una lista de evaluaciones que no han sido sincronizadas con la nube.
     * @return Una lista de objetos Evaluacion no sincronizados.
     */
    @Query("SELECT * FROM evaluations WHERE syncedToCloud = 0")
    List<EvaluationEntity> getUnsyncedEvaluations();

    /**
     * Obtiene todas las evaluaciones para una clase específica que corresponden a un semestre y parcial dados.
     * Esto es útil para agrupar evaluaciones para cálculos de promedio por parcial/semestre.
     * @param classId El ID de la clase.
     * @param semester El nombre del semestre (ej., "Primer Semestre").
     * @param parcial El nombre del parcial (ej., "Primer Parcial", puede ser null).
     * @return LiveData que contiene una lista de objetos Evaluacion.
     */
    @Query("SELECT * FROM evaluations WHERE classId = :classId AND semester = :semester AND (:parcial IS NULL OR parcial = :parcial) ORDER BY dueDate ASC")
    LiveData<List<EvaluationEntity>> getEvaluationsForClassSemesterAndParcial(String classId, String semester, String parcial);
}