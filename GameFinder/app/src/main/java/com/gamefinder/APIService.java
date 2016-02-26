package com.gamefinder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Kevin on 2/26/2016.
 */
public interface APIService {
    @Headers({
            "Access-Token: XM1cZm2pMDfrAZjsiktK0g",
            "Client: 5nuZdjxKGPhQTaTvRx0Qrg",
            "Token-Type: Bearer",
            "UID: tripp@tripp-roberts.com"
    })
    @POST("users/sign_in")
    Call<LoginResponse> signIn(@Body User user);

    @Headers({
            "Access-Token: XM1cZm2pMDfrAZjsiktK0g",
            "Client: 5nuZdjxKGPhQTaTvRx0Qrg",
            "Token-Type: Bearer",
            "UID: tripp@tripp-roberts.com"
    })
    @POST("users")
    Call<SignUpResponse> signUp(@Body SignUpUser user);
}
