package database.entities;

import androidx.room.TypeConverter;

import java.sql.Date;


public class Converters {

        @TypeConverter
        public static Date toDate(Long dateLong){
            return dateLong == null ? null: new Date(dateLong);
        }

        @TypeConverter
        public static Long fromDate(java.util.Date date){
            return date == null ? null : date.getTime();
        }
    }
