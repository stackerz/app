package com.stackerz.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class OverviewFragment extends Fragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private Activity activity;

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


    public interface Callbacks {
        public void loginRequest();

    }

    public OverviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //String endpoints = getArguments().getString("endpointStr");
        //List<Endpoints> endpointsList;
        //endpointsList = EndpointsParser.parseFeed(endpoints);
        //EndpointsAdapter adapter = new EndpointsAdapter(getActivity(), R.layout.endpoint_list, endpointsList);
        //setListAdapter(adapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String authToken = JSONData.shared().getAuthtoken();
        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(this.getActivity(),this.getActivity().getApplicationContext().getSharedPreferences("Login_Credentials",0));
                this.getActivity().getSharedPreferences("Login_Credentials", Context.MODE_PRIVATE);
        String endpoints = "";
        endpoints = sharedPreferences.getString("KeystoneData",endpoints);
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.overviewTV);
        ListView listView = (ListView) rootView.findViewById(R.id.overviewLV);
        textView.setText(endpoints);
        //if (endpoints != null) {
        //    List<Endpoints> endpointsList;
        //    endpointsList = EndpointsParser.parseFeed(endpoints);
        //    EndpointsAdapter adapter = new EndpointsAdapter(getActivity(), R.layout.endpoint_list, endpointsList);
        //    listView.setAdapter(adapter);
        //}
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