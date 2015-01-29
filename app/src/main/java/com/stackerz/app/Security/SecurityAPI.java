package com.stackerz.app.Security;

import com.stackerz.app.Routers.Routers;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface SecurityAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/v2.0/security-groups")
    void getSecurityContent(Callback<Response> callback);

}
