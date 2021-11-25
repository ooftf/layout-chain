package com.ooftf.demo.layout_chain.demo3;

import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Random;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {
    Random random = new Random();
    SparseIntArray sia = new SparseIntArray();
    SparseIntArray h = new SparseIntArray();
    {
        for (int i = 0; i < 20; i++) {
                sia.put(i,random.nextInt(2));
                h.put(i,(random.nextInt(2)+1)*100);
        }
    }

    public ParentAdapter() {
        super();
    }

    /**
     * 返回 item 个数
     * @return
     */
    @Override
    public int getItemCount() {
        return sia.size()+1;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder.getItemViewType() == 0){
            ((TextView)(holder.itemView)).setHeight(h.get(position));
            holder.itemView.setBackgroundColor(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            ((TextView)(holder.itemView)).setText("i::"+position);
        }else{

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            TextView textView = new TextView(parent.getContext());
            textView.setPadding(10,10,10,10);
            textView.setGravity(Gravity.CENTER);
            return new MyViewHolder(textView);
        }else{
            PageView pageView = new PageView(parent.getContext());
            pageView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT,StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT));
            return new MyViewHolder(pageView);
        }

    }


    public int getItemViewType(int position) {
        if( position < h.size()){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        lp.setFullSpan(isFullSpan(holder.getAdapterPosition()));
    }


    public boolean isFullSpan(int position) {
        if(position == h.size()){
            return true;
        }
        return sia.get(position) == 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
