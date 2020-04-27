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


@Dao
public abstract class ReservationDAO {

    final long millisToAdd = 7_200_000; //two hours

    @Query("SELECT * FROM RESERVATION_TABLE WHERE user_id = :userId AND :parkingId = parking_id AND from_time BETWEEN :dayStart AND :dayEnd")
    public abstract List<Reservation> getReservationsPerDay(int parkingId, int userId, java.util.Date dayStart, java.util.Date dayEnd);


//    @Query("INSERT ")
//    void reserveSlot(){
//    }
//
//    @Query("SELECT")
//    void getReservationPerDay() {
//
//    }

    @Insert
    public abstract long insertReservation(Reservation reservation);


    //
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

            List<Reservation> reservationList = getReservationsPerDay(parkingId, userId, startDate, endDate);

            List<Long> reservationStartTimestampList = new ArrayList<>();
            for (int i = 0; i < reservationList.size(); i++) {
                reservationStartTimestampList.add(reservationList.get(i).fromTime.getTime());
            }


            for (int j = 0; j < reservationList.size(); j++) {
                Reservation reservation = reservationList.get(j);
                if (reservation != null) {
                    if (getNumberOfReservationsPer2Hours(reservationList, reservation) < parkingCapacity && !checkIfUserIdExistsInReservation(userId, reservationList, reservation)) {
                        emptyReservations.add(reservation.fromTime.getTime());
                    }
                }
            }


            Long dateInMs = startDate.getTime();
            for (int i = 0; i < 12; i++) {
                if (!reservationStartTimestampList.contains(dateInMs)) {
                    emptyReservations.add(dateInMs);
                }
                dateInMs = dateInMs + millisToAdd;
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
            if (reservationList.get(i).id == reservation.id)
                reservationCount++;
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
