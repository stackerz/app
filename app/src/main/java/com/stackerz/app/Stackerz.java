package com.stackerz.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.stackerz.app.Endpoints.EndpointsParser;
import com.stackerz.app.Endpoints.OverviewFragment;
import com.stackerz.app.Flavors.FlavorsAPI;
import com.stackerz.app.Flavors.FlavorsFragment;
import com.stackerz.app.Flavors.FlavorsParser;
import com.stackerz.app.Images.ImagesAPI;
import com.stackerz.app.Images.ImagesFragment;
import com.stackerz.app.Images.ImagesParser;
import com.stackerz.app.Instances.InstancesFragment;
import com.stackerz.app.Instances.NovaAPI;
import com.stackerz.app.Instances.NovaParser;
import com.stackerz.app.Networks.NetworksAPI;
import com.stackerz.app.Networks.NetworksFragment;
import com.stackerz.app.Networks.NetworksParser;
import com.stackerz.app.Routers.RoutersAPI;
import com.stackerz.app.Routers.RoutersFragment;
import com.stackerz.app.Routers.RoutersParser;
import com.stackerz.app.Security.SecurityAPI;
import com.stackerz.app.Security.SecurityFragment;
import com.stackerz.app.Security.SecurityParser;
import com.stackerz.app.Subnets.SubnetsAPI;
import com.stackerz.app.Subnets.SubnetsFragment;
import com.stackerz.app.Subnets.SubnetsParser;
import com.stackerz.app.System.ObscuredSharedPreferences;
import com.stackerz.app.System.SSLCerts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;


public class Stackerz extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public Bundle extras, novaExtras, flavorsExtras, glanceExtras, networksExtras, routersExtras, subnetsExtras, securityExtras;
    public ArrayList<HashMap<String, String>> jsonList;
    public ArrayList<HashMap<String, String>> novaList;
    public ArrayList<HashMap<String, String>> flavorsList;
    public ArrayList<HashMap<String, String>> imagesList;
    public ArrayList<HashMap<String, String>> networksList;
    public ArrayList<HashMap<String, String>> routersList;
    public ArrayList<HashMap<String, String>> subnetsList;
    public ArrayList<HashMap<String, String>> securityList;
    public ProgressDialog pDialog;
    public String endpoints = "";
    public String authToken = "";
    public String instances = "";
    public String instancesCached = "";
    public String flavors = "";
    public String flavorsCached = "";
    public String images = "";
    public String imagesCached = "";
    public String networks = "";
    public String networksCached = "";
    public String routers = "";
    public String routersCached = "";
    public String subnets = "";
    public String subnetsCached = "";
    public String security = "";
    public String securityCached = "";
    public int first = 0;
    public int auth = 0;
    public int offline = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SSLCerts.sslHandling();
        setContentView(R.layout.activity_stackerz);

        //DISABLE THIS AFTER TESTING SYNC CALLS

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        first = 1;
        auth =1;
        pDialog = new ProgressDialog(this);
        // have to do it a couple of times because Volley sucks at this!
        for (int i = 0; i < 1; i++) {
            novaBundle();
            flavorsBundle();
            glanceBundle();
            networksBundle();
            routersBundle();
            subnetsBundle();
            securityBundle();
        }
        first = 0;
        auth = 0;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    public Bundle authBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        extras = new Bundle();
        extras.putSerializable("ParsedList", jsonList);
        return extras;
    }

    public Bundle novaBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 2;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        //instances = NovaJSON.shared().receiveData(novaURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(novaURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        NovaAPI api = adapter.create(NovaAPI.class);
        if (instances == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }

        try {
            Response result = api.getNovaSync();
            instances = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        novaExtras = new Bundle();
        if (instances != null && !instances.contains("Bad URL")) {
            /*if (auth == 0 && instances.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                instancesCached = shPref.getString("Instances", instances);
                novaList = NovaParser.parseJSON(instancesCached);
                novaExtras.putSerializable("NovaParsed", novaList);
                auth =1;
                if (instancesCached != null){
                    instances = instancesCached;
                }
            } else {*/
                shPref.edit().putString("Instances", instances).commit();
                novaList = NovaParser.parseJSON(instances);
                novaExtras.putSerializable("NovaParsed", novaList);
            }
        else if (firstSP.getInt("First", first) > 1 && shPref.getString("Instances", instances) != null) {
            instancesCached = shPref.getString("Instances", instances);
            novaList = NovaParser.parseJSON(instancesCached);
            novaExtras.putSerializable("NovaParsed", novaList);
        }
        if (first == 0 && (novaList == null || novaList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        return novaExtras;
    }

    public Bundle flavorsBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 3;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        //flavors = FlavorsJSON.shared().receiveData(novaURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(novaURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        FlavorsAPI api = adapter.create(FlavorsAPI.class);
        if (flavors == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }
        try {
            Response result = api.getFlavorsSync();
            flavors = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        flavorsExtras = new Bundle();
        if (flavors != null && !flavors.contains("Bad URL")) {
            /*if (auth == 0 && flavors.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                flavorsCached = shPref.getString("Flavors", flavors);
                flavorsList = FlavorsParser.parseJSON(flavorsCached);
                flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
                auth =1;
                if (flavorsCached != null){
                    flavors = flavorsCached;
                }
            } else {*/
                shPref.edit().putString("Flavors", flavors).commit();
                flavorsList = FlavorsParser.parseJSON(flavors);
                flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
            //}
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Flavors", flavors) != null) {
            flavorsCached = shPref.getString("Flavors", flavors);
            flavorsList = FlavorsParser.parseJSON(flavorsCached);
            flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
        }
        if (first == 0 && (flavorsList == null || flavorsList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        NovaParser.shared().setFlavorList(flavorsList);
        return flavorsExtras;
    }

    public Bundle glanceBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 4;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String glanceURL = EndpointsParser.getGlanceURL();
        //images = ImagesJSON.shared().receiveData(glanceURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(glanceURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        ImagesAPI api = adapter.create(ImagesAPI.class);
        if (images == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }
        try {
            Response result = api.getImagesSync();
            images = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        glanceExtras = new Bundle();
        if (images != null && !images.contains("Bad URL")) {
            /*if (auth == 0 && images.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                imagesCached = shPref.getString("Images", images);
                imagesList = ImagesParser.parseJSON(imagesCached);
                glanceExtras.putSerializable("ImagesParsed", imagesList);
                auth =1;
                if (imagesCached != null){
                    images = imagesCached;
                }
            } else {*/
                shPref.edit().putString("Images", images).commit();
                imagesList = ImagesParser.parseJSON(images);
                glanceExtras.putSerializable("ImagesParsed", imagesList);
            //}
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Images", images) != null) {
            imagesCached = shPref.getString("Images", images);
            imagesList = ImagesParser.parseJSON(imagesCached);
            glanceExtras.putSerializable("ImagesParsed", imagesList);
        }
        if (first == 0 && (imagesList == null || imagesList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        NovaParser.shared().setImagesList(imagesList);
        return glanceExtras;
    }

    public Bundle networksBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 5;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        //networks = NetworksJSON.shared().receiveData(neutronURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(neutronURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        NetworksAPI api = adapter.create(NetworksAPI.class);
        if (networks == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }
        try {
            Response result = api.getNetSync();
            networks = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        networksExtras = new Bundle();
        if (networks != null && !networks.contains("Bad URL")) {
            /*if (auth == 0 && networks.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                networksCached = shPref.getString("Networks", networks);
                networksList = NetworksParser.parseJSON(networksCached);
                networksExtras.putSerializable("NetworksParsed", networksList);
                auth =1;
                if (networksCached != null){
                    networks = networksCached;
                }
            } else {*/
                shPref.edit().putString("Networks", networks).commit();
                networksList = NetworksParser.parseJSON(networks);
                networksExtras.putSerializable("NetworksParsed", networksList);
            //}
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Networks", networks) != null) {
            networksCached = shPref.getString("Networks", networks);
            networksList = NetworksParser.parseJSON(networksCached);
            networksExtras.putSerializable("NetworksParsed", networksList);
        }
        if (first == 0 && (networksList == null || networksList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        return networksExtras;
    }

    public Bundle subnetsBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 6;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        //subnets = SubnetsJSON.shared().receiveData(neutronURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(neutronURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        SubnetsAPI api = adapter.create(SubnetsAPI.class);
        if (subnets == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }
        try {
            Response result = api.getSubnetsSync();
            subnets = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        subnetsExtras = new Bundle();
        if (subnets != null && !subnets.contains("Bad URL")) {
            /*if (auth == 0 && subnets.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                subnetsCached = shPref.getString("Subnets", subnets);
                subnetsList = SubnetsParser.parseJSON(subnetsCached);
                subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
                auth =1;
                if (subnetsCached != null){
                    subnets = subnetsCached;
                }
            } else {*/
                shPref.edit().putString("Subnets", subnets).commit();
                subnetsList = SubnetsParser.parseJSON(subnets);
                subnetsExtras.putString("SubnetsJSON", subnets);
                subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
            //}
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Subnets", subnets) != null) {
            subnetsCached = shPref.getString("Subnets", subnets);
            subnetsList = SubnetsParser.parseJSON(subnetsCached);
            subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
        }
        if (first == 0 && (subnetsList == null || subnetsList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        return subnetsExtras;
    }

    public Bundle routersBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 7;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        //routers = RoutersJSON.shared().receiveData(neutronURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(neutronURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        RoutersAPI api = adapter.create(RoutersAPI.class);
        if (routers == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }
        try {
            Response result = api.getRoutersSync();
            routers = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }
        routersExtras = new Bundle();
        if (routers != null && !routers.contains("Bad URL")) {
            /*if (auth == 0 && routers.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                routersCached = shPref.getString("Routers", routers);
                routersList = RoutersParser.parseJSON(routersCached);
                routersExtras.putSerializable("RoutersParsed", routersList);
                auth =1;
                if (routersCached != null){
                    routers = routersCached;
                }
            } else {*/
                shPref.edit().putString("Routers", routers).commit();
                routersList = RoutersParser.parseJSON(routers);
                routersExtras.putSerializable("RoutersParsed", routersList);
            //}
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Routers", routers) != null) {
            routersCached = shPref.getString("Routers", routers);
            routersList = RoutersParser.parseJSON(routersCached);
            routersExtras.putSerializable("RoutersParsed", routersList);
        }
        if (first == 0 && (routersList == null || routersList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        return routersExtras;
    }


    public Bundle securityBundle() {
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        int drawerPos = 8;
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken", authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        //security = SecurityJSON.shared().receiveData(neutronURL, authToken);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(3, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(neutronURL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-Auth-Token", authToken);
            }
        });
        RestAdapter adapter = builder.build();
        SecurityAPI api = adapter.create(SecurityAPI.class);
        if (security == null){
            pDialog.setMessage("Contacting Server...");
            pDialog.show();
        }

        try {
            Response result = api.getSecSync();
            security = getRawJSON(result);
        } catch (RetrofitError e) {
            Log.d("Retrofit Error", e.toString());
            if (e.toString().contains("Unauthorized")){
                tokenExpiredAlert();
            }
            if (offline==0 && (e.toString().contains("Unable to resolve host") || e.toString().contains("failed"))){
                offlineAlert();
            }
        }

        /*api.getSecurityContent(new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                security = getRawJSON(result);
                pDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Retrofit Error", error.toString());
            }
        });*/
        securityExtras = new Bundle();
        if (security != null && !security.contains("Bad URL")) {
            /*if (auth == 0 && security.contains("com.android.volley.AuthFailureError")) {
                tokenExpiredAlert();
                securityCached = shPref.getString("Security", security);
                securityList = SecurityParser.parseJSON(securityCached);
                securityExtras.putSerializable("SecurityParsed", securityList);
                auth =1;
                if (securityCached != null){
                    security = securityCached;
                }
            } else {*/
                shPref.edit().putString("Security", security).commit();
                securityList = SecurityParser.parseJSON(security);
                securityExtras.putSerializable("SecurityParsed", securityList);
           // }
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Security", security) != null) {
            securityCached = shPref.getString("Security", security);
            securityList = SecurityParser.parseJSON(securityCached);
            securityExtras.putSerializable("SecurityParsed", securityList);
        }
        if (first == 0 && (securityList == null || securityList.size() == 0)) {
            emptyViewAlert(drawerPos);
        }
        return securityExtras;
    }

    public String getRawJSON (Response response){
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
        offline = 0;
        return raw;
    }

    public void tokenExpiredAlert() {

    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    alert.setTitle("Authentication Error");
    alert.setMessage("The Authentication Token is expired! Click CONNECT to login to the server and request a new token.")
            .setNeutralButton("Connect",new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialogInterface,int i){
            Intent intent = new Intent(Stackerz.this, Login.class);
            startActivity(intent);
            finish();
        }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
    }

    public void emptyViewAlert(final int pos){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Retrieving Data");
        alert.setMessage("The server is taking too long to respond. Try again?")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onNavigationDrawerItemSelected(pos);
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void offlineAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Server Unreachable");
        alert.setMessage("Either the server is not responding or you're not connected to the same network as the server anymore. Now displaying offline content from the local cache.")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
        offline = 1;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
        switch (position) {
            case 0:
                extras = authBundle();
                OverviewFragment overviewFragment = new OverviewFragment();
                overviewFragment.setArguments(extras);
                fragmentManager.beginTransaction().add(R.id.container, OverviewFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container,overviewFragment).commit();
                break;
            case 1:
                extras = authBundle();
                OverviewFragment overviewFragment1 = new OverviewFragment();
                overviewFragment1.setArguments(extras);
                fragmentManager.beginTransaction().add(R.id.container, OverviewFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container,overviewFragment1).commit();
                break;
            case 2:
                novaExtras = novaBundle();
                InstancesFragment instancesFragment = new InstancesFragment();
                instancesFragment.setArguments(novaExtras);
                fragmentManager.beginTransaction().add(R.id.container, InstancesFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, instancesFragment).commit();
                break;
            case 3:
                flavorsExtras = flavorsBundle();
                FlavorsFragment flavorsFragment = new FlavorsFragment();
                flavorsFragment.setArguments(flavorsExtras);
                fragmentManager.beginTransaction().add(R.id.container, FlavorsFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, flavorsFragment).commit();
                break;
            case 4:
                glanceExtras = glanceBundle();
                ImagesFragment imagesFragment = new ImagesFragment();
                imagesFragment.setArguments(glanceExtras);
                fragmentManager.beginTransaction().add(R.id.container, ImagesFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, imagesFragment).commit();
                break;
            case 5:
                networksExtras = networksBundle();
                NetworksFragment networksFragment = new NetworksFragment();
                networksFragment.setArguments(networksExtras);
                fragmentManager.beginTransaction().add(R.id.container, NetworksFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, networksFragment).commit();
                break;
            case 6:
                subnetsExtras = subnetsBundle();
                SubnetsFragment subnetsFragment = new SubnetsFragment();
                subnetsFragment.setArguments(subnetsExtras);
                fragmentManager.beginTransaction().add(R.id.container, SubnetsFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, subnetsFragment).commit();
                break;
            case 7:
                routersExtras = routersBundle();
                RoutersFragment routersFragment = new RoutersFragment();
                routersFragment.setArguments(routersExtras);
                fragmentManager.beginTransaction().add(R.id.container, RoutersFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, routersFragment).commit();
                break;
            case 8:
                securityExtras = securityBundle();
                SecurityFragment securityFragment = new SecurityFragment();
                securityFragment.setArguments(securityExtras);
                fragmentManager.beginTransaction().add(R.id.container, SecurityFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, securityFragment).commit();
                break;
            default:
                fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position)).commit();
                break;

        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.overviewSection);
                break;
            case 1:
                mTitle = getString(R.string.overviewSection);
                break;
            case 2:
                mTitle = getString(R.string.instancesSection);
                break;
            case 3:
                mTitle = getString(R.string.flavorsSection);
                break;
            case 4:
                mTitle = getString(R.string.imagesSection);
                break;
            case 5:
                mTitle = getString(R.string.networksSection);
                break;
            case 6:
                mTitle = getString(R.string.subnetsSection);
                break;
            case 7:
                mTitle = getString(R.string.routersSection);
                break;
            case 8:
                mTitle = getString(R.string.securitySection);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("Stackerz");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.stackerz, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        Dialog dialog = new Dialog(Stackerz.this);
        if (id == R.id.action_connect) {
            intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences.Editor sharedPreferences = getSharedPreferences("Login_Credentials", 0).edit();
            sharedPreferences.clear();
            sharedPreferences.commit();
            SharedPreferences.Editor firstSP = getSharedPreferences("First", first).edit();
            firstSP.clear();
            firstSP.commit();
            first = 0;
            firstSP.putInt("First",first).commit();
            intent = new Intent(this, Connect.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"Settings Soon!", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_about) {
            dialog.setContentView(R.layout.about);
            dialog.setTitle("About");
            dialog.show();
            //Toast.makeText(getApplicationContext(),"Visit http://stackerz.com", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_splash, container, false);
            return rootView;

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Stackerz) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}
