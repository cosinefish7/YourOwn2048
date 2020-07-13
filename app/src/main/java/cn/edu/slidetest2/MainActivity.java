package cn.edu.slidetest2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity {

    private Button StartBtn,ScoreBtn,QuitBtn;
    private Spinner spinner1;
    private TextView textUser,textBestPlayer,textBestPlayer_name;
    String name,theme;
    String card[];
    private MyHelper helper;
    private SQLiteDatabase mydb;
    private ImageView best_view;
    private SharedPreferences sp;
    private static final String PREFERENCE_NAME = "temp_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏标题栏

        setContentView(R.layout.activity_main);
        //setContentView(new MainView(MainActivity.this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();
        initListener();
        initDB();

        //创建侧滑菜单
        SlidingMenu menu=new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.55f);
        menu.setMenu(R.layout.menu_layout);
        menu.setBackgroundResource(R.drawable.slideback);
        menu.setBehindScrollScale(1.0f);
        menu.setBehindOffsetRes(R.dimen.menu_offset);//设置相对屏幕的偏移量
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        textUser = (TextView)findViewById(R.id.textUser);
        textBestPlayer = (TextView)findViewById(R.id.textBestPlayer);
        textBestPlayer_name = (TextView)findViewById(R.id.textBestPlayer_name);
        best_view = (ImageView)findViewById(R.id.imageView_best) ;
        sp = getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);


        textUser.setText("当前还没有玩家哦！");
        textUser.setTypeface(Typeface.createFromAsset(getAssets(),"font/chinese.ttf"));
        String name = sp.getString("Name",null);
        textUser.setText(name);
        textBestPlayer.setText("还没有最高分玩家哦！");
        setBestplaer();
        textBestPlayer.setTypeface(Typeface.createFromAsset(getAssets(),"font/gilsanub.ttf"));
        textBestPlayer.setEnabled(false);
        textBestPlayer_name.setText(queryMaxPlayer());
        textBestPlayer_name.setTypeface(Typeface.createFromAsset(getAssets(),"font/chinese.ttf"));

    }

    ///////初始化数据库/////
    private void initDB() {
        helper = new MyHelper(MainActivity.this);
        mydb = helper.getWritableDatabase();
    }

    //////////侧边栏数据获取//////////
    private void setBestplaer(){
        String result = queryMaxScore()+"";
        textBestPlayer.setText(result);
    }
    private int queryMaxScore() {
        //获取当前的分数（最高分）
        String sql = "SELECT score FROM "+ helper.TABLE_NAME ;
        Cursor cursor = mydb.rawQuery(sql, null);
        int maxscore = 0;
        if (cursor.getCount() == 0) {
            return 0;
        }
        else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(0)>maxscore){
                    maxscore = cursor.getInt(0);
                }
            }
            return maxscore;
        }
    }
    private String queryMaxPlayer(){
        String sql = "SELECT id FROM "+ helper.TABLE_NAME +" WHERE score = "+queryMaxScore();
        Cursor cursor = mydb.rawQuery(sql, null);
        String maxplayer = null;
        if (cursor.getCount() == 0) {
            return null;
        }
        else {
            while (cursor.moveToNext()) {
                maxplayer = cursor.getString(0);
                maxplayer += " ";
            }
            return maxplayer;
        }
    }

    /////////view初始化////////
    public void initView(){
        StartBtn = (Button)findViewById(R.id.StartBtn);
        ScoreBtn = (Button)findViewById(R.id.ScoreBtn);
        QuitBtn = (Button)findViewById(R.id.QuitBtn);
    }

    ////////按钮初始化////////
    public void initListener(){
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(view);
            }
        });
        ScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        QuitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (view.getId() == R.id.QuitBtn){
                    showdialog(view);
                }
            }
        });
    }

    //////////退出程序部分////////
    public void showdialog(View view) {
        mydialog mydialog = new mydialog(this, R.style.infodialog);
        //实例化mydialog设置参数( 参数1:环境上下文 (这里设置this), 参数2：导入样式R.style/样式名)
        mydialog.show();
    }

    ////////////开始游戏部分/////////////
    public void start(View view) {
        infodialog infodialog =new infodialog(this,R.style.infodialog);
        //实例化mydialog设置参数( 参数1:环境上下文 (这里设置this), 参数2：导入样式R.style/样式名)
        infodialog.show();

    }
}