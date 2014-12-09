package com.stackerz.app.Instances;

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
public class NovaAdapter extends RecyclerView.Adapter<NovaListRowHolder> {

    ArrayList<HashMap<String, String>> novaList = new ArrayList<HashMap<String, String>>();
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String FLAVOR = "flavor";
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
        novaListRowHolder.flavor.setText(e.get(FLAVOR));
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
    protected TextView flavor;
    protected String id;
    private int mOriginalHeight = 0;
    private boolean mIsViewExpanded = false;

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
        this.flavor = (TextView) view.findViewById(R.id.flavorInstance);

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
