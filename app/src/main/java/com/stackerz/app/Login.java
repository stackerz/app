package com.stackerz.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.okhttp.OkHttpClient;
import com.stackerz.app.Endpoints.EndpointsAPI;
import com.stackerz.app.Endpoints.EndpointsParser;
import com.stackerz.app.Flavors.FlavorsJSON;
import com.stackerz.app.Flavors.FlavorsParser;
import com.stackerz.app.Images.ImagesJSON;
import com.stackerz.app.Images.ImagesParser;
import com.stackerz.app.Instances.NovaJSON;
import com.stackerz.app.Instances.NovaParser;
import com.stackerz.app.Networks.NetworksJSON;
import com.stackerz.app.Networks.NetworksParser;
import com.stackerz.app.Routers.RoutersJSON;
import com.stackerz.app.Routers.RoutersParser;
import com.stackerz.app.Security.SecurityParser;
import com.stackerz.app.Subnets.SubnetsJSON;
import com.stackerz.app.Subnets.SubnetsParser;
import com.stackerz.app.System.ObscuredSharedPreferences;
import com.stackerz.app.System.SSLCerts;
import com.stackerz.app.System.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;


/**
 * Created by ed on 17/10/2014.
 */
public class Login extends Activity implements View.OnClickListener{

    public Button connect;
    public String username, password, tenant, endpoint;
    public EditText userInput, passInput, tenantInput, serverInput;
    public SharedPreferences shPref;
    public Editor toEdit;
    public JSONObject endpoints;
    public String authToken;
    public String endpointStr;
    public int first = 0;
    boolean reachable = false;
    boolean prefSaved = false;

    public ArrayList<HashMap<String, String>> jsonList;
    public ArrayList<HashMap<String, String>> instancesList;
    public ArrayList<HashMap<String, String>> flavorsList;
    public ArrayList<HashMap<String, String>> imagesList;
    public ArrayList<HashMap<String, String>> networksList;
    public ArrayList<HashMap<String, String>> routersList;
    public ArrayList<HashMap<String, String>> subnetsList;
    public ArrayList<HashMap<String, String>> securityList;

    public String instances="";
    public String flavors="";
    public String images="";
    public String networks="";
    public String routers="";
    public String subnets="";
    public String security="";

    public static Login login = null;

    public static Login shared(){
        if (login == null){
            login = new Login();
        }
        return login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SSLCerts.sslHandling();
        setContentView(R.layout.activity_login);

        //DISABLE THIS AFTER TESTING SYNC CALLS

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getInit();
        SharedPreferences firstSP = getSharedPreferences("First",first);
        if (firstSP.getInt("First",first)>0){
            shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
            serverInput.setText(shPref.getString("Endpoint", endpoint));
            tenantInput.setText(shPref.getString("Tenant", tenant));
            userInput.setText(shPref.getString("Username", username));
            passInput.requestFocus();
        }


    }

    public void getInit() {
        connect = (Button) findViewById(R.id.connectButtonLogin);
        userInput = (EditText) findViewById(R.id.userName);
        serverInput = (EditText) findViewById(R.id.server);
        passInput = (EditText) findViewById(R.id.password);
        tenantInput = (EditText) findViewById(R.id.tenant);
        serverInput.requestFocus();
        connect.setOnClickListener(this);
    }

    public void setSharedPrefs() {
        //shPref = getSharedPreferences("Login_Credentials", MODE_PRIVATE);
        shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        toEdit = shPref.edit();
        toEdit.putString("Username", username);
        toEdit.putString("Password", password);
        toEdit.putString("Endpoint", endpoint);
        toEdit.putString("Tenant", tenant);
        toEdit.commit();

    }

    //public SharedPreferences getShPref() {
    //    return shPref;
    //}

    public void getSharedPrefs() {
        String storedUser = "", storedPass = "", storedURL = "", storedTenant = "", storedEndpoint ="";
        shPref.getString("Username", storedUser);
        shPref.getString("Password", storedPass);
        shPref.getString("Endpoint", storedURL);
        shPref.getString("Tenant", storedTenant);
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        shPref.edit().putString("AuthToken",authToken).commit();
    }

    public void setEndpoints(JSONObject endpoints) {
        this.endpoints = endpoints;
    }

    public JSONObject getEndpoints() {
        return endpoints;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEndpointStr() {
        return endpointStr;
    }

    public void setEndpointStr(String endpointStr) {
        this.endpointStr = endpointStr;
        shPref.edit().putString("KeystoneData",endpointStr).commit();
    }

    @Override
    public void onClick(View v) {
        v.getId();
        //v.setBackground(R.drawable.rounded_red);
        //v.setBackgroundColor(Color.RED);
        username = userInput.getText().toString();
        password = passInput.getText().toString();
        endpoint = serverInput.getText().toString();
        tenant = tenantInput.getText().toString();
        if (isNetworkAvailable()) {
            if (isValidUrl(endpoint)) {
                reachable = true;
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This is not a valid URL address, make sure there are no blank spaces at the end. Please try again.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                //serverInput.setText("");
                reachable = false;
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You don't seem to be connected to the network now. Please try again later.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            //serverInput.setText("");
            reachable = false;
        }
        if (username.isEmpty() || password.isEmpty() || tenant.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Keystone needs to know who you are. Check your user name, tenant and password.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            //serverInput.setText("");
            reachable = false;
        }
        if (reachable) {
            setSharedPrefs();
            prefSaved = true;
            loginRequest();
            if (reachable) {
                SharedPreferences firstSP = getSharedPreferences("First", first);
                Intent intent = new Intent(Login.this, Stackerz.class);
                intent.putExtra("AuthToken", authToken);
                startActivityForResult(intent,1);
                setupCache();
                first = (firstSP.getInt("First",first));
                first++;
                firstSP.edit().putInt ("First", first).commit();
            }
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if (m.matches())
            return true;
        else
            return false;
    }

    public void loginRequest() {
        final String user = shPref.getString("Username", username);
        final String pass = shPref.getString("Password", password);
        final String url = shPref.getString("Endpoint", endpoint);
        final String tnt = shPref.getString("Tenant", tenant);
        final String json = "{\"auth\": {\"tenantName\": \"" + tnt + "\", \"passwordCredentials\": {\"username\": \"" + user + "\", \"password\": \"" + pass + "\"}}}";
        final ProgressDialog pDialog = new ProgressDialog(this);


        //Handler handler = new Handler();
        //handler.postDelayed(new Runnable() {
        //    public void run() {
        //        pDialog.dismiss();
        //    }
        //}, 5000);  // 5000 milliseconds


        JSONObject login = null;
        try {
            login = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        pDialog.setMessage("Loading...");
        pDialog.show();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()));
        RestAdapter adapter = builder.build();
        EndpointsAPI api = adapter.create(EndpointsAPI.class);
        try {
            TypedInput in = new TypedByteArray("application/json", json.getBytes());
            retrofit.client.Response result = api.getEndpointsSync(in);
            JSONObject raw = null;
            try {
                raw = new JSONObject(getRawJSON(result));
                JSONObject access = raw.getJSONObject("access");
                JSONObject token = access.getJSONObject("token");
                String id = token.getString("id");
                setAuthToken(id);
                Toast.makeText(getApplicationContext(), "New Authentication Token acquired", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            setEndpoints(raw);
            setEndpointStr(raw.toString());
            reachable = true;
            pDialog.dismiss();
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Cannot connect, wrong user name or password. Please try to login again", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            if (e.toString().contains("Unable to resolve host")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "The server is not reachable. Please try again later.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            }
        }
    }
        /*
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, login,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject access = null;
                        try {
                            access = response.getJSONObject("access");
                            JSONObject token = access.getJSONObject("token");
                            String id = token.getString("id");
                            setAuthToken(id);
                            //JSONData.shared().setAuthtoken(id);
                            Toast.makeText(getApplicationContext(), "New Authentication Token acquired", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        //Log.d("App", response.toString());
                        setEndpoints(response);
                        setEndpointStr(response.toString());
                        reachable = true;
                        pDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("App", "Error: " + error.getMessage());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Cannot connect, wrong user name or password. Please try to login again", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        pDialog.dismiss();
                        reachable = false;
                        finishActivity(1);
                        Intent intent = new Intent(Login.this, Connect.class);
                        startActivity(intent);

                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "stackerz");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };


        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        queue.add(getRequest);
        pDialog.setMessage("Loading...");
        pDialog.show();

    }*/

    public String getRawJSON (retrofit.client.Response response){
        String raw = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        raw = sb.toString();
        return raw;
    }


    public void setupCache(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        endpointStr = getEndpointStr();
        authToken = getAuthToken();
        if (endpointStr == null || authToken == null) {
            endpointStr = shPref.getString("KeystoneData", endpointStr);
            authToken = shPref.getString("AuthToken", authToken);
            }
        if (endpointStr != null || authToken != null) {
            jsonList = EndpointsParser.parseJSON(endpointStr);
            EndpointsParser.shared().getURLs(jsonList);

            String novaURL = EndpointsParser.getNovaURL();
            String glanceURL = EndpointsParser.getGlanceURL();
            String neutronURL = EndpointsParser.getNeutronURL();

            /*if (novaURL != null) {
                instances = NovaJSON.shared().receiveData(novaURL, authToken);
                flavors = FlavorsJSON.shared().receiveData(novaURL, authToken);
            }
            if (glanceURL != null){
                images = ImagesJSON.shared().receiveData(glanceURL, authToken);
            }
            if (neutronURL != null) {
                networks = NetworksJSON.shared().receiveData(neutronURL, authToken);
                subnets = SubnetsJSON.shared().receiveData(neutronURL, authToken);
                routers = RoutersJSON.shared().receiveData(neutronURL, authToken);
                //security = SecurityJSON.shared().receiveData(neutronURL, authToken);
            }

            if (instances != null) {
                shPref.edit().putString("Instances", instances).commit();
                instancesList = NovaParser.parseJSON(instances);
            }
            if (flavors != null) {
                shPref.edit().putString("Flavors", flavors).commit();
                flavorsList = FlavorsParser.parseJSON(flavors);
            }
            if (images != null) {
                shPref.edit().putString("Images", images).commit();
                imagesList = ImagesParser.parseJSON(images);
            }
            if (networks != null) {
                shPref.edit().putString("Networks", networks).commit();
                networksList = NetworksParser.parseJSON(networks);
            }
            if (subnets != null) {
                shPref.edit().putString("Subnets", subnets).commit();
                subnetsList = SubnetsParser.parseJSON(subnets);
            }
            if (routers != null) {
                shPref.edit().putString("Routers", routers).commit();
                routersList = RoutersParser.parseJSON(routers);
            }
            if (security != null) {
                shPref.edit().putString("Security", security).commit();
                securityList = SecurityParser.parseJSON(security);
            }*/
        }
    }
}