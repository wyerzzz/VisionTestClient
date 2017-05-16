package com.zzz.wyer.coolpc.wyerzzz_visiontest;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private TextView tvEmpty;
    private DBSQL myRecord;
    private ArrayList<RecordBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        myRecord = DBSQL.getInstance(this);
        findViews();
        setRecyView();
    }

    private void findViews() {
        tvEmpty = (TextView) findViewById(R.id.tv_empty);
    }

    private void setRecyView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        list = getRecordData();
        final RecordAdapter adapter = new RecordAdapter(list);
        adapter.setOnItemLongClickListener(new RecordAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                Log.d("LongClick dataPosition", position + "");
                final RecordBean recordBean = list.get(position);
                new AlertDialog.Builder(RecordActivity.this)
                        .setMessage("Delete this data?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String detelePosition = recordBean.getC_id();
                                myRecord.dataDelete(detelePosition);
                                Toast.makeText(RecordActivity.this, "Deleted",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("del/dataid", detelePosition);
                                list.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position,adapter.getItemCount());
                            }
                        })
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (list.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
    private ArrayList<RecordBean> getRecordData() {
        ArrayList<RecordBean> arrayList = new ArrayList<>();
        Log.d("getdataTag", "tag");
        Cursor c = myRecord.dataQuery();
        int row = c.getCount();
        c.moveToFirst();
        for (int i = 0; i < row; i++) {
            String dataid = c.getString(0);
            String datetime = c.getString(1);
            String name = c.getString(2);
            String l_vision = c.getString(3);
            String r_vision = c.getString(4);
            RecordBean bean = new RecordBean(dataid,datetime, name, l_vision, r_vision);
            arrayList.add(bean);
            c.moveToNext();
        }
        return arrayList;
    }
}
