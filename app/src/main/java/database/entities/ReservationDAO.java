package database.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;
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
import models.TimeReservationModel;


@Dao
public abstract class ReservationDAO {

    //        @Query("SELECT * FROM RESERVATION_TABLE WHERE user_id = :userId AND :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    //    public abstract List<Reservation> getReservationsPerDay(int parkingId, int userId, java.util.Date dayStart, java.util.Date dayEnd);
    @Query("SELECT * FROM RESERVATION_TABLE WHERE  user_id = :userId AND  :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerDay(int parkingId, int userId, java.util.Date dayStart, java.util.Date dayEnd);


//    @Query("SELECT from_time, to_time, user_id, parking_id, id, rowid , count(*) AS countt FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd GROUP BY from_time, to_time ")

    @Query("SELECT * FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerParking(int parkingId, java.util.Date dayStart, java.util.Date dayEnd);

//    @Query("SELECT reservation_table.*, car_user.*  FROM RESERVATION_TABLE LEFT JOIN car_user ON RESERVATION_TABLE.user_id = car_user.id WHERE :parkingId = parking_id AND from_time == :dayStart")
//    public abstract List<Reservation> getReservationsPerParkingByStartTime(int parkingId, java.util.Date dayStart);

    @Query("SELECT *  FROM RESERVATION_TABLE WHERE :parkingId = parking_id AND from_time == :dayStart")
    public abstract List<Reservation> getReservationsPerParkingByStartTime(int parkingId, java.util.Date dayStart);


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

    @Transaction


    public List<Reservation> getReservationsByParkingIdWithCorrespondingUser(int parkingId, java.util.Date dayStart, java.util.Date dayEnd) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        List<Reservation> reservationList = getReservationsPerParking(parkingId, dayStart, dayEnd);

        for (int i = 0; i < reservationList.size(); i++) {
            reservationList.get(i).carUser = databaseHelper.getCarUserDAO().selectUserById(reservationList.get(i).userId);
            reservationList.get(i).parkingOwner = databaseHelper.getParkingDAO().selectParkingById(reservationList.get(i).parkingId);
        }


        return reservationList;

    }

    public List<Reservation> getReservationsByParkingIdAndTimeWithCorrespondingUser(int parkingId, java.util.Date dayStart) {
        DatabaseHelper databaseHelper = new DatabaseHelper();

        List<Reservation> reservationList = getReservationsPerParkingByStartTime(parkingId, dayStart);

        for (int i = 0; i < reservationList.size(); i++) {
            reservationList.get(i).carUser = databaseHelper.getCarUserDAO().selectUserById(reservationList.get(i).userId);
            reservationList.get(i).parkingOwner = databaseHelper.getParkingDAO().selectParkingById(reservationList.get(i).parkingId);
        }


        return reservationList;

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Transaction
    public List<TimeReservationModel> getAvailableSlots(int parkingId, int parkingCapacity, int userId, Long date) {


        List<TimeReservationModel> emptyReservations = new ArrayList<>();

        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String dateString = formatterDate.format(new Date(date));


        try {
            java.util.Date startDay = formatterDateTime.parse(dateString + " 00:00:00.0");
            java.util.Date endDay = formatterDateTime.parse(dateString + " 23:59:59");

            Date startDate = new Date(startDay.getTime());
            Date endDate = new Date(endDay.getTime());


//            List<Reservation> reservationList = getReservationsPerDay(parkingId, userId , startDate, endDate);
            List<Reservation> allResrvationsPerParkingPerDay = getReservationsPerParking(parkingId, startDate, endDate);

//            List<Long> reservationStartTimestampList = new ArrayList<>();
//            for (int i = 0; i < allResrvationsPerParkingPerDay.size(); i++) {
//                reservationStartTimestampList.add(allResrvationsPerParkingPerDay.get(i).fromTime.getTime());
//            }


            for (int j = 0; j < allResrvationsPerParkingPerDay.size(); j++) {
                Reservation reservation = allResrvationsPerParkingPerDay.get(j);

                if (reservation != null && !emptyReservations.stream().anyMatch(item -> reservation.fromTime.getTime() == (item.getStartTime()))) {

                    TimeReservationModel timeReservationModel;
                    if (getNumberOfReservationsPer2Hours(allResrvationsPerParkingPerDay, reservation) < parkingCapacity && !checkIfUserIdExistsInReservation(userId, reservation) && !checkIfReservationIsInPast(reservation.toTime.getTime())) {

                        timeReservationModel = new TimeReservationModel(reservation.fromTime.getTime(), true);

                        if (!emptyReservations.contains(timeReservationModel)) { //TODO check for problem/error!!!!
                            emptyReservations.add(timeReservationModel);
                        }
                    } else {
                        timeReservationModel = new TimeReservationModel(reservation.fromTime.getTime(), false);
                        emptyReservations.add(timeReservationModel);
                    }
                }
            }


            Long dateInMs = startDate.getTime();

            for (int i = 0; i < 12; i++) {
                TimeReservationModel timeReservationModel;

                if (checkIfReservationIsInPast(dateInMs + Constants.millisToAdd)) {
                    timeReservationModel = new TimeReservationModel(dateInMs, false);

                } else {

                    timeReservationModel = new TimeReservationModel(dateInMs, true);

                }

                if (!emptyReservations.stream().anyMatch(item -> timeReservationModel.getStartTime().equals(item.getStartTime()))) {
                    emptyReservations.add(timeReservationModel);
                }


                dateInMs = dateInMs + Constants.millisToAdd;
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                emptyReservations.sort((e1, e2) -> new Long(e1.getStartTime()).compareTo(new Long(e2.getStartTime())));
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

    private boolean checkIfReservationIsInPast(Long startTime) {

        if (startTime - System.currentTimeMillis() < 0) {
            return true;
        }

        return false;
    }

    private Boolean checkIfUserIdExistsInReservation(int userId, Reservation reservation) {
//        Boolean userIdExists = false;
//        for (int i = 0; i < reservationList.size(); i++) {
        return reservation.userId == userId;
//            return true;
//        else return false;
//
////        }
//        return userIdExists;
    }
}
