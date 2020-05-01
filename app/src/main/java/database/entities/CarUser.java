package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_user")
public class CarUser {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "plate_number")
    public String plateNumber;

    @ColumnInfo(name = "car_type")
    public String carType;

}
