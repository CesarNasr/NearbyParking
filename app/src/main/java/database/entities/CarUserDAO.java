package database.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class CarUserDAO {

    @Insert
    public abstract long insert(CarUser user);

    @Query("SELECT * FROM car_user WHERE user_name = :userName")
    public abstract CarUser selectUser(String userName);


    @Query("SELECT * FROM car_user WHERE user_name = :userName AND password = :password")
    public abstract CarUser loginUser(String userName, String password);


    @Transaction
    public long insertUser(CarUser carUser) {

            if (selectUser(carUser.userName) != null) {
            return -1;
        }else {
           return insert(carUser);
        }
    }
}
