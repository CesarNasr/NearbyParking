package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reservation_table")
public abstract class Reservation {

    @PrimaryKey(autoGenerate = true)
    int id;


    @ColumnInfo(name = "parking_id")
    int parkingId;


    @ColumnInfo(name = "user_id")
    int userId;

    //    @TypeConverters({Converters.class})
    @ColumnInfo(name = "from_time")
    public String fromTimeStamp;


    //    @TypeConverters({Converters.class})
    @ColumnInfo(name = "to_time")
    public String toTimeStamp;
}