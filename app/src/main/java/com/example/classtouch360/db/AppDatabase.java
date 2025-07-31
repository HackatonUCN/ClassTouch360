package com.example.classtouch360.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.classtouch360.db.DAO.AttendanceDao;
import com.example.classtouch360.db.DAO.ClassDao;
import com.example.classtouch360.db.DAO.EvaluationDao;
import com.example.classtouch360.db.DAO.StadisticsDao;
import com.example.classtouch360.db.DAO.StudentDao;
import com.example.classtouch360.db.DAO.StudentGradesDao;
import com.example.classtouch360.db.DAO.UserDao;
import com.example.classtouch360.db.entity.AttendanceEntity;
import com.example.classtouch360.db.entity.ClassEntity;
import com.example.classtouch360.db.entity.EvaluationEntity;
import com.example.classtouch360.db.entity.StudentEntity;
import com.example.classtouch360.db.entity.StudentGradesEntity;
import com.example.classtouch360.db.entity.UserEntity;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Clase principal de la base de datos Room para ClassTouch360.
 * Define las entidades (tablas) y los DAOs (objetos de acceso a datos).
 */
@Database(
        // Lista todas las clases de entidad que forman parte de esta base de datos.
        // Asegúrate de añadir todas tus entidades aquí a medida que las vayas creando.
        entities = {
                UserEntity.class,
                ClassEntity.class,
                StudentEntity.class,
                AttendanceEntity.class,
                EvaluationEntity.class,
                StudentGradesEntity.class,

                // Cuando crees las otras entidades, añádelas aquí:

        },
        // La versión de la base de datos.
        // ¡IMPORTANTE!: Incrementa este número cada vez que modifiques el esquema de la base de datos
        // (ej., añadir/eliminar tablas, columnas, cambiar tipos de datos).
        // Si incrementas la versión, necesitarás implementar migraciones para preservar los datos existentes.
        version = 2,
        // Mantenlo en 'true' para generar el esquema JSON de tu base de datos en tiempo de compilación.
        // Esto es útil para depurar y para escribir migraciones.
        exportSchema = false
)
@TypeConverters({LocalDateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // Métodos abstractos para obtener las instancias de los DAOs.
    // Room generará las implementaciones de estos DAOs por nosotros.
    public abstract UserDao userDao();
    public abstract ClassDao classDao();
    public abstract StudentDao studentDao();
    public abstract AttendanceDao attendanceDao();
    public abstract EvaluationDao evaluationDao();
    public abstract StudentGradesDao studentGradesDao();
    public abstract StadisticsDao stadisticsDao();

    // Patrón Singleton para asegurar que solo haya una instancia de la base de datos en toda la aplicación.
    // 'volatile' asegura que la variable 'INSTANCE' sea siempre leída de la memoria principal.
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Obtiene la instancia única de la base de datos.
     * Si la instancia no existe, la crea de forma segura en un bloque sincronizado
     * para evitar que múltiples hilos creen múltiples instancias.
     *
     * @param context El contexto de la aplicación (preferiblemente applicationContext).
     * @return La instancia de AppDatabase.
     */
    public static AppDatabase getDatabase(final Context context) {
        // Si la instancia ya existe, la devuelve.
        if (INSTANCE == null) {
            // Sincroniza el bloque para asegurar que solo un hilo pueda ejecutarlo a la vez.
            synchronized (AppDatabase.class) {
                // Doble chequeo para asegurar que la instancia no se haya creado mientras esperábamos.
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(), // Usa el contexto de la aplicación para evitar fugas de memoria.
                                    AppDatabase.class,
                                    "classtouch360_database" // Nombre del archivo de la base de datos en el dispositivo.
                            )
                            // Añade tus objetos Migration aquí si cambias la versión de la base de datos.
                            .addMigrations(MIGRATION_1_2)
                            // Para permitir consultas en el hilo principal (solo para depuración, NO para producción):
                            // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Ejemplo de Migración: se recomienda usar la misma lógica que se usó para el otro DAO.
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Ejemplo de cómo se vería una migración si hubieras añadido nuevas columnas.
            // Si agregas nuevas columnas a tu entidad EvaluationEntity, las agregarías aquí.
            // database.execSQL("ALTER TABLE `EvaluationEntity` ADD COLUMN `newColumn` TEXT;");
        }
    };
}
