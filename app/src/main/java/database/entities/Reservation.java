package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "reservation_table")
public abstract class Reservation {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "p_id")
    int parkingId;

    @ColumnInfo(name = "u_id")
    int userId;

//    @TypeConverters({Converters.class})
    public String fromTimeStamp;


//    @TypeConverters({Converters.class})
    public String toTimeStamp;
}