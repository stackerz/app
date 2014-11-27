package com.stackerz.app.Routers;

/**
 * Created by limedv0 on 27/11/2014.
 */
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stackerz.app.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ed on 19/11/14.
 */
public class RoutersAdapter extends RecyclerView.Adapter<RoutersListRowHolder> {

    ArrayList<HashMap<String, String>> routersList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String SNAT = "snat";
    public static final String IP = "ip";
    public static final String ID = "id";
    private Context mContext;

    public RoutersAdapter(Context context, ArrayList<HashMap<String, String>> routersList) {
        this.routersList = routersList;
        this.mContext = context;
    }

    @Override
    public RoutersListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.routers_list, null);
        RoutersListRowHolder mh = new RoutersListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(RoutersListRowHolder routersListRowHolder, int i) {

        HashMap<String, String> e = routersList.get(i);
        routersListRowHolder.name.setText(e.get(NAME));
        if (e.get(STATE) == "true"){
            routersListRowHolder.state.setText("active");
        }else{
            routersListRowHolder.state.setText("down");
        }
        if (e.get(SNAT) == "true"){
            routersListRowHolder.snat.setText("source NAT");
        }else{
            routersListRowHolder.snat.setText("no sNAT");
        }
        routersListRowHolder.ip.setText(e.get(IP));
        routersListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != routersList ? routersList.size() : 0);
    }
}

class RoutersListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView state;
    protected TextView snat;
    protected TextView ip;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public RoutersListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameRouter);
        this.state = (TextView) view.findViewById(R.id.stateRouter);
        this.snat = (TextView) view.findViewById(R.id.snatRouter);
        this.ip = (TextView) view.findViewById(R.id.ipRouter);

    }

    public void onClick(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.routers_list);
        dialog.setTitle("Details " + name.getText() + " " + getPosition());
        dialog.show();
    }

}


