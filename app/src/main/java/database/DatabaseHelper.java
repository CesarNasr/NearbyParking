package database;

import android.os.AsyncTask;

import com.example.nearbyparking.MyApplication;

import java.util.List;

import database.entities.CarUser;
import database.entities.CarUserDAO;
import database.entities.Parking;
import database.entities.ParkingDAO;
import database.entities.Reservation;
import database.entities.ReservationDAO;

public class DatabaseHelper {
    CarUserDAO carUserDAO;
    ParkingDAO parkingDAO;
    ReservationDAO reservationDAO;

    public DatabaseHelper() {
        this.carUserDAO = MyApplication.getDatabase().carUserDAO();
        this.parkingDAO = MyApplication.getDatabase().parkingDAO();
        this.reservationDAO = MyApplication.getDatabase().reservationDAO();
    }

    public ReservationDAO getReservationDAO() {
        return reservationDAO;
    }

    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public CarUserDAO getCarUserDAO() {
        return carUserDAO;
    }

    public ParkingDAO getParkingDAO() {
        return parkingDAO;
    }

    public static class InsertUser extends AsyncTask<Void, Void, Long> {
        private CarUser user;
        private CarUserDAO carUserDAO;
        private UserDBListener userDBListener;

        public InsertUser(CarUser user, CarUserDAO carUserDAO, UserDBListener userDBListener) {
            this.user = user;
            this.carUserDAO = carUserDAO;
            this.userDBListener = userDBListener;
        }

        @Override
        protected Long doInBackground(Void... Avoid) {
            return carUserDAO.insertUser(user);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if (id == -1) {
                userDBListener.onFailure();
            } else {
                userDBListener.onSuccess(user, id);
            }
        }
    }

//    selectParkingByArea

    public static class GetParkingsByArea extends AsyncTask<Void, Void, List<Parking>> {
        private ParkingDAO parkingDAO;
        private String area;
        private ParkingsDBListener parkingsDBListener;

        public GetParkingsByArea(String area, ParkingDAO parkingDAO, ParkingsDBListener parkingsDBListener) {
            this.area = area;
            this.parkingDAO = parkingDAO;
            this.parkingsDBListener = parkingsDBListener;
        }

        @Override
        protected List<Parking> doInBackground(Void... Avoid) {
            return parkingDAO.selectParkingByArea(area);
        }

        @Override
        protected void onPostExecute(List<Parking> parkings) {
            super.onPostExecute(parkings);

            if (parkings == null) {
                parkingsDBListener.onFailure();
            } else {
                parkingsDBListener.onSuccess(parkings);
            }
        }
    }


    public static class InsertOwner extends AsyncTask<Void, Void, Long> {
        private ParkingDAO parkingDAO;
        private Parking parking;
        private ParkingOwnerDBListener parkingOwnerDBListener;

        public InsertOwner(Parking parking, ParkingDAO parkingDAO, ParkingOwnerDBListener parkingOwnerDBListener) {
            this.parking = parking;
            this.parkingDAO = parkingDAO;
            this.parkingOwnerDBListener = parkingOwnerDBListener;
        }

        @Override
        protected Long doInBackground(Void... Avoid) {
            return parkingDAO.insertParkingUser(parking);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);

            if (id == -1) {
                parkingOwnerDBListener.onFailure();
            } else {
                parkingOwnerDBListener.onSuccess(parking, id);
            }
        }
    }


    public static class LoginParkingOwner extends AsyncTask<Void, Void, Parking> {
        private ParkingDAO parkingDAO;
        private String parkingUserName, parkingPassword;
        private ParkingOwnerDBListener parkingOwnerDBListener;

        public LoginParkingOwner(String parkingUserName, String parkingPassword, ParkingDAO parkingDAO, ParkingOwnerDBListener parkingOwnerDBListener) {
            this.parkingPassword = parkingPassword;
            this.parkingUserName = parkingUserName;
            this.parkingDAO = parkingDAO;
            this.parkingOwnerDBListener = parkingOwnerDBListener;
        }

        @Override
        protected Parking doInBackground(Void... Avoid) {
            return parkingDAO.loginParkingUser(parkingUserName, parkingPassword);
        }

        @Override
        protected void onPostExecute(Parking parking) {
            super.onPostExecute(parking);

            if (parking == null) {
                parkingOwnerDBListener.onFailure();
            } else {
                parkingOwnerDBListener.onSuccess(parking, -1);
            }
        }
    }


    public static class LoginUser extends AsyncTask<Void, Void, CarUser> {
        private CarUserDAO carUserDAO;
        private String userName, password;
        private UserDBListener userDBListener;

        public LoginUser(String userName, String password, CarUserDAO carUserDAO, UserDBListener userDBListener) {
            this.password = password;
            this.userName = userName;
            this.carUserDAO = carUserDAO;
            this.userDBListener = userDBListener;
        }

        @Override
        protected CarUser doInBackground(Void... Avoid) {
            return carUserDAO.loginUser(userName, password);
        }

        @Override
        protected void onPostExecute(CarUser user) {
            super.onPostExecute(user);

            if (user == null) {
                userDBListener.onFailure();
            } else {
                userDBListener.onSuccess(user, -1);
            }
        }
    }

    public static class InsertReservation extends AsyncTask<Void, Void, Long> {
        private ReservationDAO reservationDAO;
        private Reservation reservation;
        private ReserveDBListener reserveDBListener;

        public InsertReservation(Reservation reservation, ReservationDAO reservationDAO, ReserveDBListener reserveDBListener) {
            this.reservationDAO = reservationDAO;
            this.reservation = reservation;
            this.reserveDBListener = reserveDBListener;
        }

        @Override
        protected Long doInBackground(Void... Avoid) {
            return reservationDAO.insertReservation(reservation);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);

            if (id == -1) {
                reserveDBListener.onFailure();
            } else {
                reserveDBListener.onSuccess(id);
            }
        }
    }

    public static class GetAvailableParkingTimes extends AsyncTask<Void, Void, List<Long>> { // TODO getAvailableSlots change it all
        private ReservationDAO reservationDAO;
        private int userId, parkingId, parkingCapacity;
        private Long date;

        private EmptySlotsDBListener emptySlotsDBListener;

        public GetAvailableParkingTimes(int parkingCapacity, int userId, int parkingId, Long date, ReservationDAO reservationDAO, EmptySlotsDBListener emptySlotsDBListener) {
            this.reservationDAO = reservationDAO;
            this.userId = userId;
            this.parkingId = parkingId;
            this.parkingCapacity = parkingCapacity;
            this.date = date;
            this.emptySlotsDBListener = emptySlotsDBListener;
        }

        @Override
        protected List<Long> doInBackground(Void... Avoid) {
            List<Long> list = reservationDAO.getAvailableSlots(parkingId, parkingCapacity, userId, date);
//            List<Reservation> availableReservations = new ArrayList<>();


            return list;
        }

        @Override
        protected void onPostExecute(List<Long> reservations) {
            super.onPostExecute(reservations);

            if (reservations == null) {
                emptySlotsDBListener.onFailure();
            } else {
                emptySlotsDBListener.onSuccess(reservations);
            }
        }
    }


    public interface UserDBListener {
        void onSuccess(CarUser user, long id);

        void onFailure();
    }

    public interface ParkingOwnerDBListener {
        void onSuccess(Parking parkingOwner, long id);

        void onFailure();
    }

    public interface ParkingsDBListener {
        void onSuccess(List<Parking> parkings);

        void onFailure();
    }


    public interface ReserveDBListener {
        void onSuccess(Long inserted);

        void onFailure();
    }


    public interface EmptySlotsDBListener {
        void onSuccess(List<Long> emptyReservationsTimeOnly);

        void onFailure();
    }
}


