package com.stackerz.app.Instances;

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
public class NovaAdapter extends RecyclerView.Adapter<NovaListRowHolder> {

    ArrayList<HashMap<String, String>> novaList = new ArrayList<HashMap<String, String>>();
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String ID = "id";
    private Context mContext;

    public NovaAdapter(Context context, ArrayList<HashMap<String, String>> novaList) {
        this.novaList = novaList;
        this.mContext = context;
    }

    @Override
    public NovaListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.instances_list, null);
        NovaListRowHolder mh = new NovaListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(NovaListRowHolder novaListRowHolder, int i) {

        HashMap<String, String> e = novaList.get(i);
        novaListRowHolder.name.setText(e.get(NAME));
        novaListRowHolder.status.setText(e.get(STATUS));
        novaListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != novaList ? novaList.size() : 0);
    }
}

class NovaListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView status;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NovaListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameInstance);
        this.status = (TextView) view.findViewById(R.id.statusInstance);

    }

    public void onClick(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.instances_list);
        dialog.setTitle("Details " + name.getText() + " " + getPosition());
        dialog.show();
    }

}
