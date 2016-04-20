package com.gamefinder;

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
 *
 * API Service to manage GET, POST, and PUT requests from the server to the Android app.
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
    @GET("user/preferences/league")
    Call<List<PreferencesResponse>> getLeaguesPrefs(@Header("Access-Token")String accessToken,
                                           @Header("Client")String client,
                                           @Header("UID")String uid);

    @Headers("Token-Type: Bearer")
    @GET("user/preferences/competitor")
    Call<List<PreferencesResponse>> getCompetitorsPrefs(@Header("Access-Token")String accessToken,
                                                    @Header("Client")String client,
                                                    @Header("UID")String uid);

    @Headers("Token-Type: Bearer")
    @GET("leagues/{id}/competitors")
    Call<List<CompetitorsResponse>> getCompetitors(@Path("id") String id, @Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid);

    @Headers({"Token-Type: Bearer",
            "Content-Type: application/json"})
    @GET("user/games")
    Call<List<GamesResponse>> getGames(@Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid);

    @Headers({"Token-Type: Bearer",
            "Content-Type: application/json"})
    @PUT("user/preferences")
    Call<List<PreferencesResponse>> putPreferences(@Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid, @Body PreferenceBody preference);

    @Headers({"Token-Type: Bearer",
            "Content-Type: application/json"})
    @POST("user/televisions")
    Call<List<TelevisionResponse>> postTelevisions(@Header("Access-Token")String accessToken,
                                             @Header("Client")String client,
                                             @Header("UID")String uid, @Body TelevisionBody television);

    @Headers("Token-Type: Bearer")
    @GET("user/televisions")
    Call<List<TelevisionResponse>> getTelevisions(@Header("Access-Token")String accessToken,
                                           @Header("Client")String client,
                                           @Header("UID")String uid);

    @Headers("Token-Type: Bearer")
    @GET("user/channels")
    Call<List<ChannelResponse>> getChannels(@Header("Access-Token")String accessToken,
                                                  @Header("Client")String client,
                                                  @Header("UID")String uid);

    @Headers({"Token-Type: Bearer",
            "Content-Type: application/json"})
    @POST("user/channels")
    Call<List<ChannelResponse>> postChannels(@Header("Access-Token")String accessToken,
                                                   @Header("Client")String client,
                                                   @Header("UID")String uid, @Body ChannelResponse channels);
}