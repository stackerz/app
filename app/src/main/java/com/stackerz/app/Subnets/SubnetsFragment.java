package com.stackerz.app.Subnets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stackerz.app.Login;
import com.stackerz.app.R;
import com.stackerz.app.Routers.RoutersAdapter;
import com.stackerz.app.Stackerz;
import com.stackerz.app.System.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.stackerz.app.Subnets.SubnetsParser.OnJSONLoaded;

public class SubnetsFragment extends Fragment implements OnJSONLoaded{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    public ArrayList<HashMap<String, String>> jsonList;
    public RecyclerView recyclerView;
    public ProgressDialog pDialog;
    public String subnetsJSON;
        /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SubnetsFragment newInstance(int sectionNumber) {
        SubnetsFragment fragment = new SubnetsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    public SubnetsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle extras = getArguments();
        Serializable parsedList = extras.getSerializable("SubnetsParsed");
        subnetsJSON = extras.getString("SubnetsJSON");
        jsonList = (ArrayList<HashMap<String, String>>)parsedList;
        View rootView = inflater.inflate(R.layout.fragment_subnets, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.subnetsRV);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.supportsPredictiveItemAnimations();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setClickable(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        SubnetsAdapter subnetsAdapter = new SubnetsAdapter(getActivity(),jsonList);
        recyclerView.setAdapter(subnetsAdapter);
        //onJsonLoaded(jsonList);
        //if (subnetsJSON != null) {
        //    SubnetsParser.parseJSON(subnetsJSON);
        //}
    }

    @Override
    public void onJsonLoaded(ArrayList<HashMap<String, String>> list) {

        SubnetsParser.setOnJSONLoadedListener(new OnJSONLoaded() {
            @Override
            public void onJsonLoaded(ArrayList<HashMap<String, String>> list) {
                if (list.size() != 0){
                    SubnetsAdapter subnetsAdapter = new SubnetsAdapter(getActivity(),jsonList);
                    recyclerView.setAdapter(subnetsAdapter);
                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("This is taking too long!");
                    alert.setMessage("It seems there's a problem with your connection, check the server. It's also possible " +
                            "the Authentication Token expired. Please try to login again.")
                            .setNeutralButton("Connect", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getActivity(), Login.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                    getFragmentManager().beginTransaction().remove(SubnetsFragment.this).commit();
                                }
                            });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }
        });

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