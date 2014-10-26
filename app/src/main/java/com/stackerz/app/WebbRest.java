package com.stackerz.app;

import android.content.SyncAdapterType;

import com.android.volley.toolbox.Volley;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONObject;

/**
 * Created by ed on 25/10/14.
 */
public class WebbRest{
    // create the client (one-time, can be used from different threads)
    Webb webb = Webb.create();
    webb.setBaseUri(SyncPreferences.REST_ENDPOINT);
    webb.setDefaultHeader(Webb.HDR_USER_AGENT, Const.UA);

    // later we authenticate
    Response<JSONObject> response = webb
            .post("/session")
            .param("authentication", createAuthentication(syncPreferences))
            .param("deviceId", syncPrefs.getDeviceId())
            .ensureSuccess()
            .asJsonObject();

    JSONObject apiResult = response.getBody();

    AccessToken accessToken = new AccessToken();
    accessToken.token = apiResult.getString("token");
    accessToken.validUntil = apiResult.getLong("validUntil");

    webb.setDefaultHeader(HDR_ACCESS_TOKEN, accessToken.token);

    JSONObject sync = webb.post("/startSync")
            .param("lastSync", syncPrefs.getLastSync())
            .ensureSuccess()
            .asJsonObject()
            .getBody();

// ... etc. etc.

// releaseAccessToken
    webb.delete("/session").asVoid();
    accessToken = null;

}
