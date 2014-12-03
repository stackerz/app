package com.stackerz.app.Networks;

/**
 * Created by limedv0 on 27/11/2014.
 */
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
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
    private int mOriginalHeight = 0;
    private boolean mIsViewExpanded = false;

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

    public void onClick(final View view){
        if (mOriginalHeight == 0) {
            mOriginalHeight = view.getHeight();
        }
        ValueAnimator valueAnimator;
        if (!mIsViewExpanded) {
            mIsViewExpanded = true;
            valueAnimator = ValueAnimator.ofInt(mOriginalHeight, mOriginalHeight + (int) (mOriginalHeight * 1.5));
        } else {
            mIsViewExpanded = false;
            valueAnimator = ValueAnimator.ofInt(mOriginalHeight + (int) (mOriginalHeight * 1.5), mOriginalHeight);
        }
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            }
        });
        valueAnimator.start();

    }

}

