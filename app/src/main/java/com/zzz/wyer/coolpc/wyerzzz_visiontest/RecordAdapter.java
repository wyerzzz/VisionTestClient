package com.zzz.wyer.coolpc.wyerzzz_visiontest;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coolpc on 2017/5/15.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private ArrayList<RecordBean> list;
    private int data_count = 1;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public RecordAdapter(ArrayList<RecordBean> list){
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_recorditem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Log.d("recordAdapter:",position+"");
        RecordBean bean = list.get(position);

        holder.col_id.setText(data_count+"");
        holder.col_dateTime.setText(bean.getC_dateTime());
        holder.col_name.setText(bean.getC_name());
        holder.col_Leye.setText(bean.getC_l_vision());
        holder.col_Reye.setText(bean.getC_r_vision());

        String fl = holder.col_Leye.getText().toString().trim();
        String fr = holder.col_Reye.getText().toString().trim();

        changeTextColor(fl,holder.col_Leye);
        changeTextColor(fr,holder.col_Reye);
        data_count++;

        holder.itemView.setTag(position);

        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(v, (int) v.getTag());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView col_dateTime;
        private final TextView col_name;
        private final TextView col_Leye;
        private final TextView col_Reye;
        private final TextView col_id;
        public ViewHolder(View itemView) {
            super(itemView);
            col_dateTime = (TextView)itemView.findViewById(R.id.col_dateTime);
            col_name = (TextView)itemView.findViewById(R.id.col_name);
            col_Leye = (TextView)itemView.findViewById(R.id.col_Leye);
            col_Reye = (TextView)itemView.findViewById(R.id.col_Reye);
            col_id = (TextView)itemView.findViewById(R.id.col_id);
        }
    }

    private void changeTextColor(String v,TextView textView){
        v = textView.getText().toString().trim();
        if (!(v.equals(""))){
            if (v.equals("< 0.1")){
                textView.setTextColor(Color.RED);
            }else if (Float.parseFloat(v)<0.8f){
                textView.setTextColor(Color.RED);
            }else if (Float.parseFloat(v)>=1.5f){
                textView.setTextColor(Color.rgb(0,123,46));
            }
        }
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
}