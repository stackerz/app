package com.stackerz.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ed on 4/11/14.
 */
public class EndpointsAdapter extends ArrayAdapter<Endpoints> {

    private Context context;
    private List<Endpoints> endpointsList;

    public EndpointsAdapter(Context context, int resource, List<Endpoints> objects){
        super(context, resource, objects);
        this.context = context;
        this.endpointsList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.endpoint_list, parent, false);
        Endpoints endpoints = endpointsList.get(position);
        TextView tv = (TextView)view.findViewById(R.id.endpoint);
        tv.setText(endpoints.getName());

        return view;
    }
}
