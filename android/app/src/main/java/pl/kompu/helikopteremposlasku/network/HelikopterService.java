package pl.kompu.helikopteremposlasku.network;

import java.util.ArrayList;

import pl.kompu.helikopteremposlasku.model.HistoryList;
import pl.kompu.helikopteremposlasku.model.PostResponse;
import pl.kompu.helikopteremposlasku.model.Ride;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.model.UserStat;
import pl.kompu.helikopteremposlasku.model.VersionResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Kompu on 2015-09-22.
 */
public interface HelikopterService {

    @GET("/api/people/")
    Call<ArrayList<User>> getPeople(@Header("Authorization") String auth);

    @GET("/api/stats/")
    Call<ArrayList<UserStat>> getStats(@Header("Authorization") String auth);

    @GET("/api/rides/{year}/{month}")
    Call<ArrayList<Ride>> getRides(@Header("Authorization") String auth,
                                   @Path("year") int year,
                                   @Path("month") int month);

    @POST("/api/rides/{year}/{month}/{day}")
    Call<PostResponse> postRides(@Header("Authorization") String auth,
                                 @Path("year") int year,
                                 @Path("month") int month,
                                 @Path("day") int day,
                                 @Body ArrayList<Ride> rides);

    @GET("/api/version/")
    Call<VersionResponse> getActualVersion(@Header("Authorization") String auth);

    @GET("/api/history/")
    Call<HistoryList> getHistoryList(@Header("Authorization") String auth,
                                     @Query("page") int page);
}
