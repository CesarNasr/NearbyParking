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

    @ColumnInfo(name = "from_time")
    public Date fromTime;

    @ColumnInfo(name = "to_time")
    public Date toTime;


    @Ignore
    public CarUser carUser;

    @Ignore
    public Parking parkingOwner;


}