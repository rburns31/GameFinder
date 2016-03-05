package com.gamefinder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by Kevin on 2/26/2016.
 */
public interface APIService {
    @POST("users/sign_in")
    Call<LoginResponse> signIn(@Body User user);

    @POST("users")
    Call<SignUpResponse> signUp(@Body SignUpUser user);

    @Headers("Token-Type: Bearer")
    @GET("leagues")
    Call<List<LeaguesResponse>> getLeagues(@Header("Access-Token")String accessToken,
                                     @Header("Client")String client,
                                     @Header("UID")String uid);

/*    @PUT("users/preferences")
    Call<UserPreferencesResponse> preferences()*/
}
