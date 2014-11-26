package com.stackerz.app.Images;

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
public class ImagesAdapter extends RecyclerView.Adapter<ImagesListRowHolder> {

    ArrayList<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String VISIBILITY = "visibility";
    public static final String FORMAT = "format";
    private Context mContext;

    public ImagesAdapter(Context context, ArrayList<HashMap<String, String>> imageList) {
        this.imageList = imageList;
        this.mContext = context;
    }

    @Override
    public ImagesListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.images_list, null);
        ImagesListRowHolder mh = new ImagesListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ImagesListRowHolder imageListRowHolder, int i) {

        HashMap<String, String> e = imageList.get(i);
        imageListRowHolder.name.setText(e.get(NAME));
        imageListRowHolder.status.setText(e.get(STATUS));
        imageListRowHolder.visibility.setText(e.get(VISIBILITY));
        imageListRowHolder.format.setText("format: " + e.get(FORMAT));


    }


    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }
}

class ImagesListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView status;
    protected TextView visibility;
    protected TextView format;



    public ImagesListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameImage);
        this.status = (TextView) view.findViewById(R.id.statusImage);
        this.visibility = (TextView) view.findViewById(R.id.visibility);
        this.format = (TextView) view.findViewById(R.id.formatImage);

    }

    public void onClick(View view){
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.images_list);
        dialog.setTitle("Details " + name.getText() + " " + getPosition());
        dialog.show();
    }

}

