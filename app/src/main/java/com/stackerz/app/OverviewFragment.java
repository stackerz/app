package com.stackerz.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverviewFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;



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

    private ArrayList<HashMap<String, String>> jsonList;
    private RecyclerView recyclerView;
    //String endpoints = "{\"access\": {\"token\": {\"issued_at\": \"2014-11-12T02:42:16.728871\", \"expires\": \"2014-11-12T03:42:16Z\", \"id\": \"f412556ed5864e818cce14cec2a30774\", \"tenant\": {\"description\": \"Admin\", \"enabled\": true, \"id\": \"8c5cb915f5b94614a77261678ec96fb3\", \"name\": \"admin\"}, \"audit_ids\": [\"HyzB4lcwSOWbFJfVqW_S0Q\"]}, \"serviceCatalog\": [{\"endpoints\": [{\"adminURL\": \"http://stack1:8774/v2/8c5cb915f5b94614a77261678ec96fb3\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8774/v2/8c5cb915f5b94614a77261678ec96fb3\", \"id\": \"1af616c72a784f6ebb53bc09277990d7\", \"publicURL\": \"http://stack1:8774/v2/8c5cb915f5b94614a77261678ec96fb3\"}], \"endpoints_links\": [], \"type\": \"compute\", \"name\": \"nova\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:9696\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:9696\", \"id\": \"8e08ef5ad87d433f9f6d9e2772c87b1a\", \"publicURL\": \"http://stack1:9696\"}], \"endpoints_links\": [], \"type\": \"network\", \"name\": \"neutron\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:8776/v2/8c5cb915f5b94614a77261678ec96fb3\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8776/v2/8c5cb915f5b94614a77261678ec96fb3\", \"id\": \"7f178d0575b24de4bb733baf399cac30\", \"publicURL\": \"http://stack1:8776/v2/8c5cb915f5b94614a77261678ec96fb3\"}], \"endpoints_links\": [], \"type\": \"volumev2\", \"name\": \"cinderv2\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:8779/v1.0/8c5cb915f5b94614a77261678ec96fb3\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8779/v1.0/8c5cb915f5b94614a77261678ec96fb3\", \"id\": \"3fed164601394eb089badf19fd87878f\", \"publicURL\": \"http://stack1:8779/v1.0/8c5cb915f5b94614a77261678ec96fb3\"}], \"endpoints_links\": [], \"type\": \"database\", \"name\": \"trove\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:9292\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:9292\", \"id\": \"92607c050dfe4887aeaf3bcb29f4d713\", \"publicURL\": \"http://stack1:9292\"}], \"endpoints_links\": [], \"type\": \"image\", \"name\": \"glance\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:8000/v1\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8000/v1\", \"id\": \"0924abbced324f83932bb80560513aaf\", \"publicURL\": \"http://stack1:8000/v1\"}], \"endpoints_links\": [], \"type\": \"cloudformation\", \"name\": \"heat-cfn\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:8776/v1/8c5cb915f5b94614a77261678ec96fb3\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8776/v1/8c5cb915f5b94614a77261678ec96fb3\", \"id\": \"3e82f8320da64c9887d5e41ef776f4c4\", \"publicURL\": \"http://stack1:8776/v1/8c5cb915f5b94614a77261678ec96fb3\"}], \"endpoints_links\": [], \"type\": \"volume\", \"name\": \"cinder\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:8004/v1/8c5cb915f5b94614a77261678ec96fb3\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:8004/v1/8c5cb915f5b94614a77261678ec96fb3\", \"id\": \"bbfab906f3b84574ba7c6af09051bfe8\", \"publicURL\": \"http://stack1:8004/v1/8c5cb915f5b94614a77261678ec96fb3\"}], \"endpoints_links\": [], \"type\": \"orchestration\", \"name\": \"heat\"}, {\"endpoints\": [{\"adminURL\": \"http://stack1:35357/v2.0\", \"region\": \"regionOne\", \"internalURL\": \"http://stack1:5000/v2.0\", \"id\": \"6df8444747e14e8fab8050681bed9319\", \"publicURL\": \"http://stack1:5000/v2.0\"}], \"endpoints_links\": [], \"type\": \"identity\", \"name\": \"keystone\"}], \"user\": {\"username\": \"admin\", \"roles_links\": [], \"id\": \"fa92eb3a32b34d5c9f8ccb1acdf3a87a\", \"roles\": [{\"name\": \"_member_\"}, {\"name\": \"admin\"}], \"name\": \"admin\"}, \"metadata\": {\"is_admin\": 0, \"roles\": [\"9fe2ff9ee4384b1894a90878d3e92bab\", \"4f4f26e53a894daa94da1c6f9d7f0dc3\"]}}}";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null && b.containsKey("AuthToken")){
            String authToken = b.getString("AuthToken");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String authToken = JSONData.shared().getAuthtoken();


        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.overviewRV);

        return rootView;
        }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(this.getActivity(), this.getActivity().getApplicationContext().getSharedPreferences("Login_Credentials", 0));
        String endpoints = "";
        endpoints = sharedPreferences.getString("KeystoneData", endpoints);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.endpoint_list);
                        dialog.setTitle("Details " + recyclerView.getChildItemId(view) + " " + position);
                        dialog.show();
                        //Toast.makeText(getActivity(), "Test" + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );
        jsonList = EndpointsParser.parseJSON(endpoints);
        EndpointsParser.shared().getURLs(jsonList);
        String novaURL = EndpointsParser.getNovaURL();
        EndpointsAdapter endpointsAdapter = new EndpointsAdapter(getActivity(),jsonList);
        recyclerView.setAdapter(endpointsAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OverviewCallbacks{
        public void onItemSelected(Endpoints endpoints);
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