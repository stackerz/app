package com.stackerz.app.Flavors;

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
public class FlavorsAdapter extends RecyclerView.Adapter<FlavorsListRowHolder> {

    ArrayList<HashMap<String, String>> flavorList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String RAM = "ram";
    public static final String VCPUS = "vcpus";
    public static final String DISK = "disk";
    public static final String ID = "id";
    private Context mContext;

    public FlavorsAdapter(Context context, ArrayList<HashMap<String, String>> flavorList) {
        this.flavorList = flavorList;
        this.mContext = context;
    }

    @Override
    public FlavorsListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.flavors_list, null);
        FlavorsListRowHolder mh = new FlavorsListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(FlavorsListRowHolder flavorListRowHolder, int i) {

        HashMap<String, String> e = flavorList.get(i);
        flavorListRowHolder.name.setText(e.get(NAME));
        flavorListRowHolder.vcpus.setText(e.get(VCPUS)+" "+"vCPUs");
        flavorListRowHolder.ram.setText(e.get(RAM) + "Mb" + " " + "RAM");
        flavorListRowHolder.disk.setText(e.get(DISK) + "Gb" + " " + "disk");
        flavorListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != flavorList ? flavorList.size() : 0);
    }
}

class FlavorsListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView vcpus;
    protected TextView ram;
    protected TextView disk;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public FlavorsListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameFlavor);
        this.vcpus = (TextView) view.findViewById(R.id.vcpusFlavor);
        this.ram = (TextView) view.findViewById(R.id.ramFlavor);
        this.disk = (TextView) view.findViewById(R.id.diskFlavor);

    }

    public void onClick(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.flavors_list);
        dialog.setTitle("Details " + name.getText() + " " + getPosition());
        dialog.show();
    }

}
