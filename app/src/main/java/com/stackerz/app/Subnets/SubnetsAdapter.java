package com.stackerz.app.Subnets;

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
public class SubnetsAdapter extends RecyclerView.Adapter<SubnetsListRowHolder> {

    ArrayList<HashMap<String, String>> subnetsList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String GW = "gw";
    public static final String CIDR = "cidr";
    public static final String ID = "id";
    private Context mContext;

    public SubnetsAdapter(Context context, ArrayList<HashMap<String, String>> subnetsList) {
        this.subnetsList = subnetsList;
        this.mContext = context;
    }

    @Override
    public SubnetsListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subnets_list, null);
        SubnetsListRowHolder mh = new SubnetsListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SubnetsListRowHolder subnetsListRowHolder, int i) {

        HashMap<String, String> e = subnetsList.get(i);
        subnetsListRowHolder.name.setText(e.get(NAME));
        subnetsListRowHolder.gw.setText("gw: " + e.get(GW));
        subnetsListRowHolder.cidr.setText(e.get(CIDR));
        subnetsListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != subnetsList ? subnetsList.size() : 0);
    }
}

class SubnetsListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView gw;
    protected TextView cidr;
    protected String id;
    private int mOriginalHeight = 0;
    private boolean mIsViewExpanded = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public SubnetsListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameSubnet);
        this.gw = (TextView) view.findViewById(R.id.gwSubnet);
        this.cidr = (TextView) view.findViewById(R.id.cidrSubnet);

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



