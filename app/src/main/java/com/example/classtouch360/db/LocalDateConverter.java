package com.example.classtouch360.db;


import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Clase de convertidor de tipos para Room,
 * utilizada para manejar objetos LocalDate.
 */
public class LocalDateConverter {

    /**
     * Convierte un timestamp (Long) almacenado en la base de datos a un objeto LocalDate.
     * @param timestamp El timestamp en milisegundos.
     * @return El objeto LocalDate correspondiente o null si el timestamp es null.
     */
    @TypeConverter
    public static LocalDate toLocalDate(Long timestamp) {
        return timestamp == null ? null : Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Convierte un objeto LocalDate a un timestamp (Long) para ser almacenado en la base de datos.
     * @param date El objeto LocalDate a convertir.
     * @return El timestamp en milisegundos o null si el objeto LocalDate es null.
     */
    @TypeConverter
    public static Long fromLocalDate(LocalDate date) {
        return date == null ? null : date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
