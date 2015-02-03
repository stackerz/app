package com.stackerz.app.Instances;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface NovaDetailAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/servers/{id}")
    Response getNovaDetailSync(@Path("id") String id);

}
