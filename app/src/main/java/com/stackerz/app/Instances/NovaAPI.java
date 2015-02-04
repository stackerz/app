package com.stackerz.app.Instances;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface NovaAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/servers/detail")
    void getNovaAsync(Callback<Response> callback);
    @GET("/servers/detail")
    Response getNovaSync();
    @GET("/servers/{id}")
    Response getNovaDetailSync(@Path("id") String id);

}

