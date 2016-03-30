package com.gamefinder;

import android.preference.Preference;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Kevin on 2/26/2016.
 */
public interface APIService {
    @POST("users/sign_in")
    Call<LoginResponse> signIn(@Body User user);

    @POST("user/television")
    Call<LoginResponse> postChannels(@Body User user,
                                     @Body Channel channel);

    @POST("users")
    Call<SignUpResponse> signUp(@Body SignUpUser user);

    @Headers("Token-Type: Bearer")
    @GET("leagues")
    Call<List<LeaguesResponse>> getLeagues(@Header("Access-Token")String accessToken,
                                     @Header("Client")String client,
                                     @Header("UID")String uid);

    @Headers("Token-Type: Bearer")
    @GET("leagues/{id}/competitors")
    Call<List<CompetitorsResponse>> getCompetitors(@Path("id") String id, @Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid);

    @Headers({"Token-Type: Bearer",
            "Content-Type: application/json"})
    @PUT("user/preferences")
    Call<List<PreferencesResponse>> putPreferences(@Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid, @Body PreferenceBody preference);


/*    @PUT("users/preferences")
    Call<UserPreferencesResponse> preferences()*/
}
