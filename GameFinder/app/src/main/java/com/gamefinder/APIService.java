package com.gamefinder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Kevin on 2/26/2016.
 */
public class APIService {
    @POST("/users/sign_in")
    Call<User> signIn(@Body User user);
}
