package database.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class ParkingDAO {

    @Insert
    public abstract long insert(Parking parking);

    @Query("SELECT * FROM parking_table WHERE user_name = :userName")
    public abstract Parking selectParkingByUsername(String userName);

    @Query("SELECT * FROM parking_table WHERE area_name LIKE :area")
    public abstract List<Parking> selectParkingByArea(String area);

    @Query("SELECT * FROM parking_table WHERE user_name = :userName AND password = :password")
    public abstract Parking loginParkingUser(String userName, String password);

    @Transaction
    public long insertParkingUser(Parking parkingUser) {

        if (selectParkingByUsername(parkingUser.userName) != null) {
            return -1;
        } else {
            return insert(parkingUser);
        }
    }

}
