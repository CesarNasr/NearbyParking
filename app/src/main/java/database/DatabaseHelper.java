package database;

import android.os.AsyncTask;

import com.example.nearbyparking.MyApplication;

import database.entities.CarUser;
import database.entities.CarUserDAO;
import database.entities.Parking;
import database.entities.ParkingDAO;

public class DatabaseHelper {
    CarUserDAO carUserDAO;
    ParkingDAO parkingDAO;

    public DatabaseHelper() {
        this.carUserDAO = MyApplication.getDatabase().carUserDAO();
        this.parkingDAO = MyApplication.getDatabase().parkingDAO();

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


    public interface UserDBListener {
        void onSuccess(CarUser user, long id);

        void onFailure();
    }

    public interface ParkingOwnerDBListener {
        void onSuccess(Parking parkingOwner, long id);

        void onFailure();
    }
}


