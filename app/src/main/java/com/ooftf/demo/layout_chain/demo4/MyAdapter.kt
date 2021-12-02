package com.ooftf.demo.layout_chain.demo4;

import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class MyAdapter extends StickyAdapter<MyAdapter.MyViewHolder> {
    Random random = new Random();
    SparseIntArray sia = new SparseIntArray();
    SparseIntArray h = new SparseIntArray();
    {
        for (int i = 0; i < 1000; i++) {
            if(i == getStickyPosition()){
                sia.put(i,0);
                h.put(i,200);
            }else{
                sia.put(i,random.nextInt(2));
                h.put(i,(random.nextInt(2)+1)*100);
            }


        }
    }

    public MyAdapter(@NonNull StickyRecyclerViewLayout layout) {
        super(layout);
    }


    /**
     * 返回 item 个数
     * @return
     */
    @Override
    public int getItemCount() {
        return 1000;
    }


    @Override
    public void onBindViewHolderEx(@NonNull MyViewHolder holder, int position) {
        ((TextView)(holder.itemView)).setHeight(h.get(position));
        holder.itemView.setBackgroundColor(Color.argb(255,random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        ((TextView)(holder.itemView)).setText("i::"+position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolderEx(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setPadding(10,10,10,10);
        textView.setGravity(Gravity.CENTER);
        return new MyViewHolder(textView);
    }

    /**
     * 返回 粘性布局的位置
     * @return
     */
    @Override
    public int getStickyPosition() {
        return 30;
    }

    /**
     * 返回view 的类型，所有 card 类型都是一样的，其他 根据 数据中 layout.type 类型的不同返回不同的值
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public boolean isFullSpan(int position) {
        return sia.get(position) == 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
