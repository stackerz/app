package com.stackerz.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by limedv0 on 13/11/2014.
 */
public final class EndpointListRowHolder extends RecyclerView.ViewHolder {
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
