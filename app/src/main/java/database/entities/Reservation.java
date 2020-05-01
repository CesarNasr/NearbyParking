package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "reservation_table")
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "parking_id")
    public int parkingId;


    @ColumnInfo(name = "user_id")
    public int userId;

    //    @TypeConverters({Converters.class})
    @ColumnInfo(name = "from_time")
    public Date fromTime;

//    public String fromTimeStamp;

//    val joined_date: OffsetDateTime? = null


    //    @TypeConverters({Converters.class})
    @ColumnInfo(name = "to_time")
    public Date toTime;





//    public String toTimeStamp;
}