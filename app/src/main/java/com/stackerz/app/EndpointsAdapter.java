package com.stackerz.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ed on 4/11/14.
 */
public class EndpointsAdapter extends RecyclerView.Adapter<EndpointListRowHolder> {

    ArrayList<HashMap<String, String>> endpointsList = new ArrayList<HashMap<String, String>>();
    public static final String PUBLICURL = "publicURL";
    public static final String REGION = "region";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    private Context mContext;

    public EndpointsAdapter(Context context, ArrayList<HashMap<String, String>> endpointsList) {
        this.endpointsList = endpointsList;
        this.mContext = context;
    }

    @Override
    public EndpointListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.endpoint_list, null);
        EndpointListRowHolder mh = new EndpointListRowHolder(v);
        Log.d("RV", "OnCreateViewHolder");
        return mh;
    }

    @Override
    public void onBindViewHolder(EndpointListRowHolder endpointListRowHolder, int i) {

        HashMap<String, String> e = endpointsList.get(i);
        endpointListRowHolder.name.setText(e.get(NAME));
        endpointListRowHolder.type.setText(e.get(TYPE));
        endpointListRowHolder.region.setText(e.get(REGION));
        endpointListRowHolder.url.setText(e.get(PUBLICURL));
        Log.d("RV", e.get(NAME));

    }

    @Override
    public int getItemCount() {
        return (null != endpointsList ? endpointsList.size() : 0);
    }
}

