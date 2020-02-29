package network;

import java.util.List;

import models.Root;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/maps/api/place/nearbysearch/json")
    Call<Root> getPlaces(@Query("location") String location, @Query("radius") String radius, @Query("types") String types,  @Query("sensor") String sensor, @Query("key") String key);
}


//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.8938,35.5018&radius=1000&types=parking&sensor=false&key=AIzaSyAZevZYKP_nnsPunE5ViboeLNfEJT6Vwy0
