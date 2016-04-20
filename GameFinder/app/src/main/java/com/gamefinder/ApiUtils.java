package com.gamefinder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Contains utilities for communicating with the API using retrofit
 * Includes the base url, the three headers to be passed on an API call, and the service which
 *     facilitates the connection
 * Created by Ryan on 4/2/2016.
 */
public class ApiUtils {
    public static String accessToken;
    public static String client;
    public static String uid;

    public final static String BASE_URL = "https://fathomless-woodland-78351.herokuapp.com/api/";

    public final static Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();

    public final static APIService service = retrofit.create(APIService.class);
}