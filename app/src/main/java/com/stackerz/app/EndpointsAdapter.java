package com.stackerz.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    //private List<Endpoints> endpointsList;
    private Context mContext;

    public EndpointsAdapter(Context context, ArrayList<HashMap<String, String>> endpointsList) {
        this.endpointsList = endpointsList;
        this.mContext = context;
    }

    @Override
    public EndpointListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.endpoint_list, null);
        EndpointListRowHolder mh = new EndpointListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(EndpointListRowHolder endpointListRowHolder, int i) {
        Endpoints endpoints = new Endpoints();
        endpointsList.get(i);

            endpointListRowHolder.name.setText(endpoints.getName());
            endpointListRowHolder.type.setText(endpoints.getType());
            endpointListRowHolder.region.setText(endpoints.getRegion());
            endpointListRowHolder.url.setText(endpoints.getPublicURL());
    }

    @Override
    public int getItemCount() {
        return (null != endpointsList ? endpointsList.size() : 0);
    }
}

class EndpointListRowHolder extends RecyclerView.ViewHolder {
    protected TextView name;
    protected TextView type;
    protected TextView region;
    protected TextView url;


    public EndpointListRowHolder(View view) {
        super(view);
        this.name = (TextView) view.findViewById(R.id.name);
        this.type = (TextView) view.findViewById(R.id.type);
        this.region = (TextView) view.findViewById(R.id.region);
        this.url = (TextView) view.findViewById(R.id.url);
    }

}