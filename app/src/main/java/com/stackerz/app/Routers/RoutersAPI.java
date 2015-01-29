package com.stackerz.app.Routers;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface RoutersAPI {
    @Headers({
            "X-Auth-Token: {authToken}",
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/v2.0/routers")
    public void getRoutersContent(Callback<List<Routers>> response);

}
