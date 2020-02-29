package database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import database.entities.CarUser;
import database.entities.CarUserDAO;
import database.entities.Parking;
import database.entities.ParkingDAO;
import database.entities.Reservation;
import database.entities.ReservationDAO;


@Database(entities = {Parking.class, CarUser.class, Reservation.class}, version = 1)
public abstract class ParkingsDatabase extends RoomDatabase {

    public abstract ParkingDAO parkingDAO();

    public abstract CarUserDAO carUserDAO();

    public abstract ReservationDAO reservationDAO();

}
