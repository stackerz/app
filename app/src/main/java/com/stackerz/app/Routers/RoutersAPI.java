package com.stackerz.app.Routers;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface RoutersAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/v2.0/routers")
    //public void getRoutersContent(Callback<List<Routers>> response);
    //void getRoutersContent(Callback<Response> callback);
    Response getRoutersSync();

}
