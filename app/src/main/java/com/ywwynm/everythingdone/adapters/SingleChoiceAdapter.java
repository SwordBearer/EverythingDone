package com.ywwynm.everythingdone.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ywwynm on 2015/8/19.
 * Adapter to offer single select for items.
 */
public abstract class SingleChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "SingleChoiceAdapter";

    protected int mPickedPosition = -1;

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public abstract int getItemCount();

    public void pick(int position) {
        mPickedPosition = position;
        notifyDataSetChanged();
    }

    public int getPickedPosition() {
        return mPickedPosition;
    }
}
