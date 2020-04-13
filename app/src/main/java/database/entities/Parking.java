package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parking_table")
public class Parking {

    public Parking() {

    }

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "parking_name")
    public String parkingName;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo
    public int capacity;

    @ColumnInfo(name = "address_name")
    public String parkingAddress;

    @ColumnInfo(name = "area_name")
    public String areaName;
}
