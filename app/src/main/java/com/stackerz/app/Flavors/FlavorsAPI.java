package com.stackerz.app.Flavors;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface FlavorsAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @GET("/flavors/detail")
    //void getFlavorsContent(Callback<Response> callback);
    Response getFlavorsSync();

}
