package com.stackerz.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stackerz.app.Endpoints.EndpointsParser;
import com.stackerz.app.Endpoints.OverviewFragment;
import com.stackerz.app.Flavors.FlavorsFragment;
import com.stackerz.app.Flavors.FlavorsJSON;
import com.stackerz.app.Flavors.FlavorsParser;
import com.stackerz.app.Images.ImagesFragment;
import com.stackerz.app.Images.ImagesJSON;
import com.stackerz.app.Images.ImagesParser;
import com.stackerz.app.Instances.InstancesFragment;
import com.stackerz.app.Instances.NovaJSON;
import com.stackerz.app.Instances.NovaParser;
import com.stackerz.app.Networks.NetworksFragment;
import com.stackerz.app.Networks.NetworksJSON;
import com.stackerz.app.Networks.NetworksParser;
import com.stackerz.app.Routers.RoutersFragment;
import com.stackerz.app.Routers.RoutersJSON;
import com.stackerz.app.Routers.RoutersParser;
import com.stackerz.app.Security.SecurityFragment;
import com.stackerz.app.Security.SecurityJSON;
import com.stackerz.app.Security.SecurityParser;
import com.stackerz.app.Subnets.Subnets;
import com.stackerz.app.Subnets.SubnetsFragment;
import com.stackerz.app.Subnets.SubnetsJSON;
import com.stackerz.app.Subnets.SubnetsParser;
import com.stackerz.app.System.ObscuredSharedPreferences;
import com.stackerz.app.System.SSLCerts;

import java.util.ArrayList;
import java.util.HashMap;


public class Stackerz extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

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
    public String endpoints="";
    public String authToken="";
    public String instances="";
    public String instancesCached="";
    public String flavors="";
    public String flavorsCached="";
    public String images="";
    public String imagesCached="";
    public String networks="";
    public String networksCached="";
    public String routers ="";
    public String routersCached="";
    public String subnets ="";
    public String subnetsCached="";
    public String security ="";
    public String securityCached="";
    public int first = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SSLCerts.sslHandling();
        setContentView(R.layout.activity_stackerz);
        first = 1;
        // have to do it a couple of times because Volley sucks at this!
        for (int i=0; i<3; i++) {
            novaBundle();
            flavorsBundle();
            glanceBundle();
            networksBundle();
            routersBundle();
            subnetsBundle();
            securityBundle();
        }
        first = 0;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }



    public Bundle authBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        extras = new Bundle();
        extras.putSerializable("ParsedList", jsonList);
        return extras;
    }

    public Bundle novaBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        instances = NovaJSON.shared().receiveData(novaURL, authToken);
        novaExtras = new Bundle();
        if (instances != null && !instances.contains("Bad URL")) {
            if (instances.contains("com.android.volley.AuthFailureError")){
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                instancesCached = shPref.getString("Instances",instances);
                novaList = NovaParser.parseJSON(instancesCached);
                novaExtras.putSerializable("NovaParsed", novaList);
            } else {
                shPref.edit().putString("Instances", instances).commit();
                novaList = NovaParser.parseJSON(instances);
                novaExtras.putSerializable("NovaParsed", novaList);
            }
        } else if (firstSP.getInt("First", first)>1 && shPref.getString("Instances",instances)!= null){
            instancesCached = shPref.getString("Instances",instances);
            novaList = NovaParser.parseJSON(instancesCached);
            novaExtras.putSerializable("NovaParsed", novaList);
        }
        if (first == 0 && (novaList == null || novaList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return novaExtras;
    }

    public Bundle flavorsBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        flavors = FlavorsJSON.shared().receiveData(novaURL, authToken);
        flavorsExtras = new Bundle();
        if (flavors != null && !flavors.contains("Bad URL")) {
            if (flavors.contains("com.android.volley.AuthFailureError")){
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                flavorsCached = shPref.getString("Flavors", flavors);
                flavorsList = FlavorsParser.parseJSON(flavorsCached);
                flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
            } else {
                shPref.edit().putString("Flavors", flavors).commit();
                flavorsList = FlavorsParser.parseJSON(flavors);
                flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
            }
        } else if (firstSP.getInt("First", first) > 1 && shPref.getString("Flavors", flavors)!= null) {
                flavorsCached = shPref.getString("Flavors", flavors);
                flavorsList = FlavorsParser.parseJSON(flavorsCached);
                flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
        }
        if (first == 0 && (flavorsList == null || flavorsList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        NovaParser.shared().setFlavorList(flavorsList);
        return flavorsExtras;
    }

    public Bundle glanceBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String glanceURL = EndpointsParser.getGlanceURL();
        images = ImagesJSON.shared().receiveData(glanceURL, authToken);
        glanceExtras = new Bundle();
        if (images != null && !images.contains("Bad URL")) {
            if (images.contains("com.android.volley.AuthFailureError")){
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                imagesCached = shPref.getString("Images",images);
                imagesList = ImagesParser.parseJSON(imagesCached);
                glanceExtras.putSerializable("ImagesParsed", imagesList);
            } else {
                shPref.edit().putString("Images", images).commit();
                imagesList = ImagesParser.parseJSON(images);
                glanceExtras.putSerializable("ImagesParsed", imagesList);
            }
        } else if (firstSP.getInt("First",first)>1 && shPref.getString("Images",images)!= null){
            imagesCached = shPref.getString("Images",images);
            imagesList = ImagesParser.parseJSON(imagesCached);
            glanceExtras.putSerializable("ImagesParsed", imagesList);
       }
        if (first == 0 && (imagesList == null || imagesList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return glanceExtras;
    }

    public Bundle networksBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First", first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        networks = NetworksJSON.shared().receiveData(neutronURL, authToken);
        networksExtras = new Bundle();
        if (networks != null && !networks.contains("Bad URL")) {
            if (networks.contains("com.android.volley.AuthFailureError")){
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                networksCached = shPref.getString("Networks",networks);
                networksList = NetworksParser.parseJSON(networksCached);
                networksExtras.putSerializable("NetworksParsed", networksList);
            } else {
                shPref.edit().putString("Networks",networks).commit();
                networksList = NetworksParser.parseJSON(networks);
                networksExtras.putSerializable("NetworksParsed", networksList);
            }
        } else if (firstSP.getInt("First",first)>1 && shPref.getString("Networks",networks)!= null){
            networksCached = shPref.getString("Networks",networks);
            networksList = NetworksParser.parseJSON(networksCached);
            networksExtras.putSerializable("NetworksParsed", networksList);
        }
        if (first == 0 && (networksList == null || networksList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return networksExtras;
    }

    public Bundle subnetsBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        subnets = SubnetsJSON.shared().receiveData(neutronURL, authToken);
        subnetsExtras = new Bundle();
        if (subnets != null && !subnets.contains("Bad URL")) {
            if (subnets.contains("com.android.volley.AuthFailureError")) {
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                subnetsCached = shPref.getString("Subnets",subnets);
                subnetsList = SubnetsParser.parseJSON(subnetsCached);
                subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
            } else {
                shPref.edit().putString("Subnets", subnets).commit();
                subnetsList = SubnetsParser.parseJSON(subnets);
                subnetsExtras.putString("SubnetsJSON", subnets);
                subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
            }
        } else if (firstSP.getInt("First",first)>1 && shPref.getString("Subnets",subnets)!= null){
            subnetsCached = shPref.getString("Subnets",subnets);
            subnetsList = SubnetsParser.parseJSON(subnetsCached);
            subnetsExtras.putSerializable("SubnetsParsed", subnetsList);
        }
        if (first == 0 && (subnetsList == null || subnetsList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return subnetsExtras;
    }

    public Bundle routersBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        routers = RoutersJSON.shared().receiveData(neutronURL, authToken);
        routersExtras = new Bundle();
        if (routers != null && !routers.contains("Bad URL")) {
            if (routers.contains("com.android.volley.AuthFailureError")) {
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                routersCached = shPref.getString("Routers", routers);
                routersList = RoutersParser.parseJSON(routersCached);
                routersExtras.putSerializable("RoutersParsed", routersList);
            }else {
                shPref.edit().putString("Routers", routers).commit();
                routersList = RoutersParser.parseJSON(routers);
                routersExtras.putSerializable("RoutersParsed", routersList);
            }
        } else if (firstSP.getInt("First",first)>1 && shPref.getString("Routers",routers)!= null){
            routersCached = shPref.getString("Routers",routers);
            routersList = RoutersParser.parseJSON(routersCached);
            routersExtras.putSerializable("RoutersParsed", routersList);
        }
        if (first == 0 && (routersList == null || routersList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return routersExtras;
    }


    public Bundle securityBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        SharedPreferences firstSP = getSharedPreferences("First",first);
        endpoints = shPref.getString("KeystoneData", endpoints);
        authToken = shPref.getString("AuthToken",authToken);
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String neutronURL = EndpointsParser.getNeutronURL();
        security = SecurityJSON.shared().receiveData(neutronURL, authToken);
        securityExtras = new Bundle();
        if (security != null && !security.contains("Bad URL")) {
            if (security.contains("com.android.volley.AuthFailureError")) {
                Toast.makeText(getApplicationContext(), "Authentication Token is expired! Please connect again. Offline content from the last successful session was cached and it is now being displayed.", Toast.LENGTH_LONG).show();
                securityCached = shPref.getString("Security",security);
                securityList = SecurityParser.parseJSON(securityCached);
                securityExtras.putSerializable("SecurityParsed", securityList);
            } else {
                shPref.edit().putString("Security", security).commit();
                securityList = SecurityParser.parseJSON(security);
                securityExtras.putSerializable("SecurityParsed", securityList);
            }
        } else if (firstSP.getInt("First",first)>1 && shPref.getString("Security",security)!= null){
            securityCached = shPref.getString("Security",security);
            securityList = SecurityParser.parseJSON(securityCached);
            securityExtras.putSerializable("SecurityParsed", securityList);
        }
        if (first == 0 && (securityList == null || securityList.size() == 0)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Retrieving Data");
            alert.setMessage("It's taking a while to get the data from the Server. Please select the option on the left drawer again to request it one more time.")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        return securityExtras;
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
