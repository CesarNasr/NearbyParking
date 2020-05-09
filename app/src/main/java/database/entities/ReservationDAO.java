package database.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Helpers.Constants;
import database.DatabaseHelper;


@Dao
public abstract class ReservationDAO {

    //        @Query("SELECT * FROM RESERVATION_TABLE WHERE user_id = :userId AND :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    //    public abstract List<Reservation> getReservationsPerDay(int parkingId, int userId, java.util.Date dayStart, java.util.Date dayEnd);
    @Query("SELECT * FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerDay(int parkingId, java.util.Date dayStart, java.util.Date dayEnd);


//    @Query("SELECT from_time, to_time, user_id, parking_id, id, rowid , count(*) AS countt FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd GROUP BY from_time, to_time ")

    @Query("SELECT * FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerParking(int parkingId, java.util.Date dayStart, java.util.Date dayEnd);


    @Query("SELECT * FROM RESERVATION_TABLE WHERE :userId = user_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerUserId(int userId, java.util.Date dayStart, java.util.Date dayEnd);


    @Insert
    public abstract long insertReservation(Reservation reservation);


    @Transaction

    public List<Reservation> getReservationsByUserIdWithCorrespondingUser(int userId, java.util.Date dayStart, java.util.Date dayEnd) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        List<Reservation> reservationList = getReservationsPerUserId(userId, dayStart, dayEnd);

        for (int i = 0; i < reservationList.size(); i++) {
            reservationList.get(i).carUser = databaseHelper.getCarUserDAO().selectUserById(userId);
            reservationList.get(i).parkingOwner = databaseHelper.getParkingDAO().selectParkingById(reservationList.get(i).parkingId);
        }


        return reservationList;

    }

    public List<Reservation> getReservationsByParkingIdWithCorrespondingUser(int parkingId, java.util.Date dayStart, java.util.Date dayEnd) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        List<Reservation> reservationList = getReservationsPerParking(parkingId, dayStart, dayEnd);

        for (int i = 0; i < reservationList.size(); i++) {
            reservationList.get(i).carUser = databaseHelper.getCarUserDAO().selectUserById(reservationList.get(i).userId);
            reservationList.get(i).parkingOwner = databaseHelper.getParkingDAO().selectParkingById(reservationList.get(i).parkingId);
        }


        return reservationList;

    }



    @Transaction
    public List<Long> getAvailableSlots(int parkingId, int parkingCapacity, int userId, Long date) {


        List<Long> emptyReservations = new ArrayList<>();

        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String dateString = formatterDate.format(new Date(date));


        try {
            java.util.Date startDay = formatterDateTime.parse(dateString + " 00:00:00.0");
            java.util.Date endDay = formatterDateTime.parse(dateString + " 23:59:59");

            java.sql.Date startDate = new java.sql.Date(startDay.getTime());
            java.sql.Date endDate = new java.sql.Date(endDay.getTime());


            List<Reservation> reservationList = getReservationsPerDay(parkingId, startDate, endDate);

            List<Long> reservationStartTimestampList = new ArrayList<>();
            for (int i = 0; i < reservationList.size(); i++) {
                reservationStartTimestampList.add(reservationList.get(i).fromTime.getTime());
            }


            for (int j = 0; j < reservationList.size(); j++) {
                Reservation reservation = reservationList.get(j);

                if (reservation != null) {
                    if (getNumberOfReservationsPer2Hours(reservationList, reservation) < parkingCapacity && !checkIfUserIdExistsInReservation(userId, reservationList, reservation)) {
                        if (!emptyReservations.contains(reservation.fromTime.getTime()))
                            emptyReservations.add(reservation.fromTime.getTime());
                    }
                }
            }


            Long dateInMs = startDate.getTime();
            for (int i = 0; i < 12; i++) {
                if (!reservationStartTimestampList.contains(dateInMs)) {
                    emptyReservations.add(dateInMs);
                }


                dateInMs = dateInMs + Constants.millisToAdd;
            }

            return emptyReservations;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getNumberOfReservationsPer2Hours(List<Reservation> reservationList, Reservation reservation) {
        int reservationCount = 0;
        for (int i = 0; i < reservationList.size(); i++) {

            if (reservationList.get(i).fromTime.getTime() == reservation.fromTime.getTime())
                reservationCount++;

//            if (reservationList.get(i).id == reservation.id)
        }
        return reservationCount;
    }

    private Boolean checkIfUserIdExistsInReservation(int userId, List<Reservation> reservationList, Reservation reservation) {
        Boolean userIdExists = false;
        for (int i = 0; i < reservationList.size(); i++) {
            if (reservationList.get(i).userId == userId)
                userIdExists = true;


        }
        return userIdExists;
    }
}
