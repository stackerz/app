package com.stackerz.app.Security;
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
public class SecurityAdapter extends RecyclerView.Adapter<SecurityListRowHolder> {

    ArrayList<HashMap<String, String>> securityList = new ArrayList<HashMap<String, String>>();
    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String ID = "id";
    private Context mContext;

    public SecurityAdapter(Context context, ArrayList<HashMap<String, String>> securityList) {
        this.securityList = securityList;
        this.mContext = context;
    }

    @Override
    public SecurityListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.security_list, null);
        SecurityListRowHolder mh = new SecurityListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SecurityListRowHolder securityListRowHolder, int i) {

        HashMap<String, String> e = securityList.get(i);
        securityListRowHolder.name.setText(e.get(NAME));
        securityListRowHolder.desc.setText(e.get(DESC));
        securityListRowHolder.setId(e.get(ID));


    }


    @Override
    public int getItemCount() {
        return (null != securityList ? securityList.size() : 0);
    }
}

class SecurityListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected TextView name;
    protected TextView desc;
    protected String id;
    private int mOriginalHeight = 0;
    private boolean mIsViewExpanded = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public SecurityListRowHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.name = (TextView) view.findViewById(R.id.nameSec);
        this.desc = (TextView) view.findViewById(R.id.descSec);

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
