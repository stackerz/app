package com.stackerz.app.Networks;

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
public class NetworksAdapter extends RecyclerView.Adapter<NetworksListRowHolder> {

    ArrayList<HashMap<String, String>> networkList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String EXTERNAL = "external";
    public static final String TYPE = "type";
    public static final String SHARED = "shared";
    public static final String ID = "id";
    private Context mContext;

    public NetworksAdapter(Context context, ArrayList<HashMap<String, String>> networkList) {
        this.networkList = networkList;
        this.mContext = context;
    }

    @Override
    public NetworksListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.networks_list, null);
        NetworksListRowHolder mh = new NetworksListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(NetworksListRowHolder networkListRowHolder, int i) {

        HashMap<String, String> e = networkList.get(i);
        networkListRowHolder.name.setText(e.get(NAME));
        if (e.get(STATE) == "true"){
            networkListRowHolder.state.setText("active");
        }else{
            networkListRowHolder.state.setText("down");
        }
        if (e.get(EXTERNAL) == "true"){
            networkListRowHolder.external.setText("external");
        }else{
            networkListRowHolder.external.setText("internal");
        }
        if (e.get(SHARED) == "true"){
            networkListRowHolder.shared.setText("shared");
        }else{
            networkListRowHolder.shared.setText("private");
        }
        networkListRowHolder.type.setText("type: " + e.get(TYPE));
        networkListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != networkList ? networkList.size() : 0);
    }
}

class NetworksListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView state;
    protected TextView external;
    protected TextView type;
    protected TextView shared;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public NetworksListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameNetwork);
        this.state = (TextView) view.findViewById(R.id.stateNetwork);
        this.external = (TextView) view.findViewById(R.id.externalNetwork);
        this.type = (TextView) view.findViewById(R.id.typeNetwork);
        this.shared = (TextView) view.findViewById(R.id.sharedNetwork);

    }

    public void onClick(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.networks_list);
        dialog.setTitle("Details " + name.getText() + " " + getPosition());
        dialog.show();
    }

}

