package database.entities;

import androidx.room.Dao;
import androidx.room.Query;


@Dao
public abstract class ReservationDAO {


    @Query("INSERT ")
    void reserveSlot(){
    }

    @Query("SELECT")
    void getReservationPerDay() {

    }
}
