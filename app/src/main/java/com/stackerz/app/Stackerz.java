package com.stackerz.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
    public Bundle extras, novaExtras, flavorsExtras, glanceExtras;
    public ArrayList<HashMap<String, String>> jsonList;
    public ArrayList<HashMap<String, String>> novaList;
    public ArrayList<HashMap<String, String>> flavorsList;
    public ArrayList<HashMap<String, String>> imagesList;
    public String endpoints="";
    public String authToken="";
    public String instances="";
    public String instancesCached="";
    public String flavors="";
    public String flavorsCached="";
    public String images="";
    public String imagesCached="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SSLCerts.sslHandling();
        setContentView(R.layout.activity_stackerz);
        EndpointsParser.shared().getURLs(jsonList);
        novaBundle();
        flavorsBundle();
        glanceBundle();
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
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        instances = NovaJSON.shared().receiveData(novaURL, authToken);
        novaExtras = new Bundle();
        if (instances != null) {
            shPref.edit().putString("Instances",instances).commit();
            novaList = NovaParser.parseJSON(instances);
            novaExtras.putSerializable("NovaParsed", novaList);
        } else if (shPref.getString("Instances",instances)!= null){
            instancesCached = shPref.getString("Instances",instancesCached);
            novaList = NovaParser.parseJSON(instancesCached);
            novaExtras.putSerializable("NovaParsed", novaList);
        }
        return novaExtras;
    }

    public Bundle flavorsBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        flavors = FlavorsJSON.shared().receiveData(novaURL, authToken);
        flavorsExtras = new Bundle();
        if (flavors != null) {
            shPref.edit().putString("Flavors",flavors).commit();
            flavorsList = FlavorsParser.parseJSON(flavors);
            flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
        } else if (shPref.getString("Flavors",flavors)!= null){
            flavorsCached = shPref.getString("Flavors",flavors);
            flavorsList = FlavorsParser.parseJSON(flavorsCached);
            flavorsExtras.putSerializable("FlavorsParsed", flavorsList);
        }
        return flavorsExtras;
    }

    public Bundle glanceBundle(){
        SharedPreferences shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE));
        EndpointsParser.shared().getURLs(jsonList);
        String glanceURL = EndpointsParser.getGlanceURL();
        images = ImagesJSON.shared().receiveData(glanceURL, authToken);
        glanceExtras = new Bundle();
        if (images != null) {
            shPref.edit().putString("Images",images).commit();
            imagesList = ImagesParser.parseJSON(images);
            glanceExtras.putSerializable("ImagesParsed", imagesList);
        } else if (shPref.getString("Images",images)!= null){
            imagesCached = shPref.getString("Images",images);
            imagesList = ImagesParser.parseJSON(imagesCached);
            glanceExtras.putSerializable("ImagesParsed", imagesList);
        }
        return glanceExtras;
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
                novaExtras = novaBundle();
                InstancesFragment instancesFragment = new InstancesFragment();
                instancesFragment.setArguments(novaExtras);
                fragmentManager.beginTransaction().add(R.id.container, InstancesFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, instancesFragment).commit();
                break;
            case 2:
                flavorsExtras = flavorsBundle();
                FlavorsFragment flavorsFragment = new FlavorsFragment();
                flavorsFragment.setArguments(flavorsExtras);
                fragmentManager.beginTransaction().add(R.id.container, FlavorsFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, flavorsFragment).commit();
                break;
            case 3:
                glanceExtras = glanceBundle();
                ImagesFragment imagesFragment = new ImagesFragment();
                imagesFragment.setArguments(glanceExtras);
                fragmentManager.beginTransaction().add(R.id.container, ImagesFragment.newInstance(position)).commit();
                fragmentManager.beginTransaction().replace(R.id.container, imagesFragment).commit();
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container, NetworksFragment.newInstance(position)).commit();
                break;
            case 5:
                fragmentManager.beginTransaction().replace(R.id.container, RoutersFragment.newInstance(position)).commit();
                break;
            case 6:
                fragmentManager.beginTransaction().replace(R.id.container, ProjectsFragment.newInstance(position)).commit();
                break;
            case 7:
                fragmentManager.beginTransaction().replace(R.id.container, UsersFragment.newInstance(position)).commit();
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
                mTitle = getString(R.string.instancesSection);
                break;
            case 2:
                mTitle = getString(R.string.flavorsSection);
                break;
            case 3:
                mTitle = getString(R.string.imagesSection);
                break;
            case 4:
                mTitle = getString(R.string.networksSection);
                break;
            case 5:
                mTitle = getString(R.string.routersSection);
                break;
            case 6:
                mTitle = getString(R.string.projectsSection);
                break;
            case 7:
                mTitle = getString(R.string.usersSection);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setIcon(R.drawable.logo);
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
            SharedPreferences.Editor first = getSharedPreferences("First", 0).edit();
            first.clear();
            first.commit();
            first.putBoolean("First",false).commit();
            first.putBoolean("Token",false).commit();
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
