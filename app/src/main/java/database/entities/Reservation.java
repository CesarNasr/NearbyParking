package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.text.SimpleDateFormat;

@Entity(tableName = "reservation_table")
public abstract class Reservation {

    @ColumnInfo(name = "p_id")
    int parkingId;

    @ColumnInfo(name = "u_id")
    int userId;

    @ColumnInfo(name = "from_t")
    SimpleDateFormat fromTime;

    @ColumnInfo(name = "to_t")
    SimpleDateFormat toTime;
}