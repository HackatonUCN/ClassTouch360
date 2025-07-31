package com.example.classtouch360.db.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classtouch360.db.entity.StudentEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Estudiante.
 * Define las operaciones CRUD y de consulta para la tabla de estudiantes.
 */
@Dao
public interface StudentDao {
    /**
     * Inserta un nuevo estudiante en la base de datos. Si el estudiante ya existe (por studentId),
     * lo reemplaza.
     * @param estudiante El objeto Estudiante a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEstudiante(StudentEntity estudiante);

    /**
     * Actualiza un estudiante existente en la base de datos.
     * @param estudiante El objeto Estudiante con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateEstudiante(StudentEntity estudiante);

    /**
     * Elimina un estudiante de la base de datos.
     * @param estudiante El objeto Estudiante a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteEstudiante(StudentEntity estudiante);

    /**
     * Obtiene todos los estudiantes asociados a una clase específica, ordenados por nombre.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param classId El ID de la clase.
     * @return LiveData que contiene una lista de objetos Estudiante.
     */
    @Query("SELECT * FROM students WHERE classId = :classId ORDER BY firstName ASC")
    LiveData<List<StudentEntity>> getStudentsForClass(String classId);

    /**
     * Obtiene un estudiante por su ID.
     * @param studentId El ID del estudiante.
     * @return El objeto Estudiante o null si no se encuentra.
     */
    @Query("SELECT * FROM students WHERE studentId = :studentId")
    StudentEntity getEstudianteById(String studentId);

    /**
     * Obtiene una lista de estudiantes por su nombre (búsqueda parcial).
     * @param name El nombre (o parte del nombre) a buscar.
     * @return LiveData que contiene una lista de objetos Estudiante que coinciden con el nombre.
     */
    @Query("SELECT * FROM students WHERE firstName LIKE :name  ORDER BY firstName ASC")
    LiveData<List<StudentEntity>> getStudentsByName(String name);

    /**
     * Obtiene un estudiante por su código estudiantil.
     * @param studentCode El código estudiantil a buscar.
     * @return LiveData que contiene el objeto Estudiante o null si no se encuentra.
     */
    @Query("SELECT * FROM students WHERE studentCode = :studentCode")
    LiveData<StudentEntity> getStudentByCode(String studentCode);

    /**
     * Obtiene una lista de estudiantes que no han sido sincronizados con la nube.
     * @return Una lista de objetos Estudiante no sincronizados.
     */
    @Query("SELECT * FROM students WHERE syncedToCloud = 0")
    List<StudentEntity> getUnsyncedStudents();
}