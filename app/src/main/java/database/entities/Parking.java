package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "parking_table")
public class Parking {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo
    String name;

    @ColumnInfo
    int capacity;

    @ColumnInfo(name = "owner_name")
    String ownerName;

    @ColumnInfo(name = "area_name")
    String areaName;
}
