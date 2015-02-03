package com.stackerz.app.Instances;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;

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
        //void getSecurityContent(Callback<Response> callback);
    Response getNovaSync();

}

