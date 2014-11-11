package com.stackerz.app;

import android.app.Activity;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OverviewFragment extends ListFragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private Activity activity;
    public static final String PUBLICURL = "publicURL";
    public static final String REGION = "region";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();


     /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OverviewFragment newInstance(int sectionNumber) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public OverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String authToken = JSONData.shared().getAuthtoken();
        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(this.getActivity(), this.getActivity().getApplicationContext().getSharedPreferences("Login_Credentials", 0));
        String endpoints = "";
        endpoints = sharedPreferences.getString("KeystoneData", endpoints);
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.overviewTV);
        //ListView listView = (ListView) rootView.findViewById(R.id.overviewLV);
        //textView.setText(endpoints);
        //ListAdapter adapter = EndpointsParser.shared().initJSON(endpoints);
        try {
            Endpoints endpoint = new Endpoints();
            JSONObject keystone = new JSONObject(endpoints);
            JSONObject access = keystone.getJSONObject("access");
            JSONArray serviceCatalog = access.getJSONArray("serviceCatalog");

            for (int i = 0; i < serviceCatalog.length(); i++) {
                JSONObject objsvc = serviceCatalog.getJSONObject(i);
                JSONArray endpointsArray = objsvc.getJSONArray("endpoints");
                endpoint.setName(objsvc.getString("name"));
                endpoint.setType(objsvc.getString("type"));
                for (int j = 0; j < endpoints.length(); j++) {
                    JSONObject objept = endpointsArray.getJSONObject(j);
                    endpoint.setRegion(objept.getString("region"));
                    endpoint.setPublicURL(objept.getString("publicURL"));
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, endpoint.getName());
                map.put(TYPE, endpoint.getType());
                map.put(REGION, endpoint.getRegion());
                map.put(PUBLICURL, endpoint.getPublicURL());
                jsonList.add(map);
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.d("ErrorInitJSON", e.toString());
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(getActivity(), jsonList, R.layout.endpoint_list, new String[]{NAME, TYPE, REGION, PUBLICURL}, new int[]{R.id.name, R.id.type, R.id.region, R.id.url});
        try {
            setListAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            Log.d("ErrorListAdapter", e.toString());
            e.printStackTrace();
        }
        return rootView;



        }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Stackerz) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

        //try {
        //    mListener = (OnFragmentInteractionListener) activity;
        //} catch (ClassCastException e) {
        //    throw new ClassCastException(activity.toString()
        //            + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}