package com.yefoo.demopullrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yufeng on 2019/2/16.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Student> mData = new ArrayList<>();

    public void setData(List<Student> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Student> list) {
        if (list != null && list.size() > 0) {
            int startPos = mData.size();
            mData.addAll(list);
            notifyItemRangeInserted(startPos, list.size());
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = mData.get(position);
        holder.idTv.setText(String.valueOf(student.getId()));
        holder.contentTv.setText(student.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView idTv;
        private TextView contentTv;

        MyViewHolder(View view) {
            super(view);
            idTv = view.findViewById(R.id.text_id);
            contentTv = view.findViewById(R.id.text_content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MyAdapter", "position=" + getLayoutPosition());
                }
            });
        }

    }
}
