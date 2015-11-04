package com.example.practice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private LayoutInflater mInflater;
    private Context context;
    private List<String> list;
    private ArrayList<Integer> heights = new ArrayList<>();
    private OnItemClickListener listener;


    public RecyclerViewAdapter(Context context, List<String> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;

        for (int i = 0; i < 100; i++) {
            heights.add((int) (100 + Math.random()*300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.text_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv.setText(list.get(i));
//        ViewGroup.LayoutParams layoutParams = myViewHolder.tv.getLayoutParams();
//        layoutParams.height = heights.get(i);

        if (listener != null) {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int layoutPosition = myViewHolder.getLayoutPosition();
                    listener.onItemClickListener(myViewHolder.itemView, layoutPosition);
                }
            });

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
                    listener.onItemLongClickListener(myViewHolder.itemView, myViewHolder.getLayoutPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(int i) {
        list.add(i,"new data");
        notifyItemInserted(i);
    }

    public void detele(int i) {
        list.remove(i);
        notifyItemRemoved(i);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(View view, int postion);
        void onItemLongClickListener(View view, int postion);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}


