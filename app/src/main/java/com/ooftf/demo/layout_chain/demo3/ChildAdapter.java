package com.ooftf.demo.layout_chain.demo3;

import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.MyViewHolder> {
    Random random = new Random();
    SparseIntArray sia = new SparseIntArray();
    SparseIntArray h = new SparseIntArray();
    {
        for (int i = 0; i < 200; i++) {
                sia.put(i,random.nextInt(2));
                h.put(i,(random.nextInt(2)+1)*100);
        }
    }

    public ChildAdapter() {
        super();
    }

    /**
     * 返回 item 个数
     * @return
     */
    @Override
    public int getItemCount() {
        return sia.size();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder.getItemViewType() == 0){
            ((TextView)(holder.itemView)).setHeight(h.get(position));
            holder.itemView.setBackgroundColor(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            ((TextView)(holder.itemView)).setText("i::"+position);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setPadding(10,10,10,10);
            textView.setGravity(Gravity.CENTER);
            return new MyViewHolder(textView);

    }


    public int getItemViewType(int position) {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
