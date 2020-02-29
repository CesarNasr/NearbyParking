package database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "car_user")
public class CarUser {
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "full_name")
    String fullName;

    @ColumnInfo(name = "plate_number")
    String plateNumber;

    @ColumnInfo(name = "color")
    String color;

    @ColumnInfo(name = "car_type")
    String carType;

    @ColumnInfo(name = "model_year")
    String modelYear;

}
