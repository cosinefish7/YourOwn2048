package cn.edu.slidetest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends Activity {


    private List<ScoreRate> ScoreRateList;
    private MyHelper myHelper;
    private MyAdapter myAdapter;
    private ListView lv;
    private ImageView backBtn,clearBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        initViews();
    }

    public void initViews(){
        ScoreRateList = new ArrayList<ScoreRate>();
        myHelper = new MyHelper(this);

        lv = (ListView) findViewById(R.id.listView);
        backBtn = (ImageView) findViewById(R.id.backBtn2);
        clearBtn = (ImageView) findViewById(R.id.clearBtn);

        ScoreRateList=queryAll();

        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.super.onBackPressed();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                builder.setTitle("真的不保留这些记录吗？");
                builder.setPositiveButton("确定",listener);
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
    }


    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SQLiteDatabase sqLiteDatabase = myHelper.getWritableDatabase();
            int count = sqLiteDatabase.delete("ScoreRate",null,null);
            myAdapter.notifyDataSetChanged();
            sqLiteDatabase.close();
        }
    };

    ///////查询所有排行榜////////
    private List<ScoreRate> queryAll(){
        SQLiteDatabase sqLiteDatabase = myHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("ScoreRate",null,null,null,null,null,"score Desc");
        List<ScoreRate> srlist = new ArrayList<>();
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("theme"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            srlist.add(new ScoreRate(id,name,score));
        }
        cursor.close();
        sqLiteDatabase.close();
        return srlist;
    }

    ///////设置颜色///////
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ScoreRateList.size();
        }

        @Override
        public Object getItem(int position) {
            return ScoreRateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(SecondActivity.this,R.layout.item,null);

            TextView nameTv = view.findViewById(R.id.usernameTv);
            TextView scoreTv = view.findViewById(R.id.scoreTv);
            TextView themeTv=view.findViewById(R.id.themeTv);

            ScoreRate user = ScoreRateList.get(position);
            switch (position){
                case 0:
                    scoreTv.setTextColor(0xfff67c5f);
                    //scoreTv.setTextSize(22);
                    break;
                case 1:
                    scoreTv.setTextColor(0xfff2b179);
                    //scoreTv.setTextSize(20);
                    break;
                case 2:
                    scoreTv.setTextColor(0xffedcf72);
                    break;
            }
            nameTv.setText(user.getId());
            scoreTv.setText(user.getScore()+"分");
            themeTv.setText(user.getTheme());

            return view;
        }
    }
}
