package com.stackerz.app.Endpoints;

import org.json.JSONObject;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * Created by limedv0 on 28/01/2015.
 */
public interface EndpointsAPI {
    @Headers({
            "User-Agent: stackerz",
            "Accept: application/json",
            "Content-Type: application/json; charset=utf-8"
    })
    @POST("/")
    //void getImagesContent(Callback<Response> callback);
    Response getEndpointsSync(@Body TypedInput body);

}
