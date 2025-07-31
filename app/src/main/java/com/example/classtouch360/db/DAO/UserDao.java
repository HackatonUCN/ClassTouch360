package com.example.classtouch360.db.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.classtouch360.db.entity.UserEntity;

import java.util.List;

/**
 * DAO (Data Access Object) para la entidad User.
 * Define las operaciones CRUD y de consulta para la tabla de usuarios.
 */
@Dao
public interface UserDao {
    /**
     * Inserta un nuevo usuario en la base de datos. Si el usuario ya existe (por userId),
     * lo reemplaza. Esto es útil para la lógica de sincronización (upsert).
     * @param user El objeto User a insertar o actualizar.
     * @return El ID de la fila insertada o el ID de la fila reemplazada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(UserEntity user);

    /**
     * Actualiza un usuario existente en la base de datos.
     * @param user El objeto User con los datos actualizados.
     * @return El número de filas actualizadas.
     */
    @Update
    int updateUser(UserEntity user);

    /**
     * Elimina un usuario de la base de datos.
     * @param user El objeto User a eliminar.
     * @return El número de filas eliminadas.
     */
    @Delete
    int deleteUser(UserEntity user);

    /**
     * Obtiene un usuario por su ID. Retorna LiveData para observar cambios en tiempo real.
     * @param userId El ID del usuario.
     * @return LiveData que contiene el objeto User o null si no se encuentra.
     */
    @Query("SELECT * FROM users WHERE userId = :userId")
    LiveData<UserEntity> getUserById(String userId);

    /**
     * Obtiene una lista de todos los usuarios en la base de datos.
     * @return LiveData que contiene una lista de todos los objetos User.
     */
    @Query("SELECT * FROM users ORDER BY name ASC")
    LiveData<List<UserEntity>> getAllUsers();

    /**
     * Obtiene una lista de usuarios que no han sido sincronizados con la nube.
     * @return Una lista de objetos User no sincronizados.
     */
    @Query("SELECT * FROM users WHERE syncedToCloud = 0")
    List<UserEntity> getUnsyncedUsers();

    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmail(String email);
}
