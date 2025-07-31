package com.example.classtouch360.db.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.classtouch360.db.entity.ClassEntity;
import com.example.classtouch360.db.entity.StudentEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Clase.
 * Define las operaciones CRUD y de consulta para la tabla de clases.
 */
@Dao
public interface ClassDao {
    /**
     * Inserta una nueva clase en la base de datos. Si la clase ya existe (por classId),
     * la reemplaza.
     * @param clase El objeto Clase a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertClase(ClassEntity clase);

    /**
     * Actualiza una clase existente en la base de datos.
     * @param clase El objeto Clase con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateClase(ClassEntity clase);

    /**
     * Elimina una clase de la base de datos.
     * @param clase El objeto Clase a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteClase(ClassEntity clase);

    /**
     * Obtiene todas las clases asociadas a un usuario específico, ordenadas por nombre de clase.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param userId El ID del usuario.
     * @return LiveData que contiene una lista de objetos Clase.
     */
    @Query("SELECT * FROM classes WHERE userId = :userId ORDER BY className ASC")
    LiveData<List<ClassEntity>> getClassesForUser(String userId);

    /**
     * Obtiene todas las clases asociadas a un usuario específico, ordenadas por nombre de materia.
     * Retorna LiveData para observar cambios en tiempo real.
     * @param userId El ID del usuario.
     * @return LiveData que contiene una lista de objetos Clase.
     */
    @Query("SELECT * FROM classes WHERE userId = :userId ORDER BY subjectName ASC")
    LiveData<List<ClassEntity>> getClassesForUserOrderBySubject(String userId);

    /**
     * Obtiene una clase por su ID.
     * @param classId El ID de la clase.
     * @return El objeto Clase o null si no se encuentra.
     */
    @Query("SELECT * FROM classes WHERE classId = :classId")
    ClassEntity getClaseById(String classId);

    /**
     * Obtiene una clase por su nombre de sección o por el nombre de la materia.
     * @param name El nombre de la sección o materia a buscar.
     * @return LiveData que contiene una lista de objetos Clase que coinciden con la búsqueda.
     */
    @Query("SELECT * FROM classes WHERE className LIKE :name OR subjectName LIKE :name ORDER BY className ASC")
    LiveData<List<ClassEntity>> getClassesByNameOrSubject(String name);

    /**
     * Obtiene una lista de clases que no han sido sincronizadas con la nube.
     * @return Una lista de objetos Clase no sincronizados.
     */
    @Query("SELECT * FROM classes WHERE syncedToCloud = 0")
    List<ClassEntity> getUnsyncedClasses();


}