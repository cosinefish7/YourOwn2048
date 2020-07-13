package cn.edu.slidetest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Random;

public class IndexActivity extends Activity {

    private MyHelper helper;
    private SQLiteDatabase mydb;
    int[][] numDouble;
    int[] num;
    String change;
    int score;
    TextView scoreTv,userTv,textView10;
    ImageView backBtn,reGameBtn;
    String name,theme;
    String card[];
    int cardLength;
    MyHelper myHelper;
    private Math Logarithm;
    private boolean isData  = false;
    private TextView textUser;
    private SharedPreferences sp;
    private static final String PREFERENCE_NAME = "temp_data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        initDB();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        theme = intent.getStringExtra("theme");
        card=intent.getStringArrayExtra("card");
        sp = getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Name",name);
        editor.commit();

        for(int i=0;i<card.length;i++){
            System.out.println(card.length+"!");
        }
        cardLength=card.length;
        initialize();
        userTv.setText(name);
        ViewGroupOnTouchEvent((ViewGroup) findViewById(R.id.relativeLayout));
        ///buttons
        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder1 = new AlertDialog.Builder(IndexActivity.this);
                builder1.setTitle("真的不继续了吗？");
                //    设置一个PositiveButton
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        System.out.println("右下角分数"+Integer.parseInt(scoreTv.getText().toString()));
//                        System.out.println("读数据库的分数"+queryScore(name,theme));
                        int isUserData = 0;
                        //判断有没有数据
                        String sql = "SELECT * FROM " + helper.TABLE_NAME + " WHERE id='" + name +"' AND theme = '"+theme+"'";
                        Cursor cursor = mydb.rawQuery(sql, null);
                        String result = null;
                        if (cursor.getCount() != 0) {
                            isUserData = 1;
                        }
                        if (isUserData == 0)//传进去的数据之前没有该用户的数据
                        {
                            insertData(name, theme,Integer.parseInt(scoreTv.getText().toString()));
                            //insert();
                        }
                        if (Integer.parseInt(scoreTv.getText().toString())>queryScore(name,theme)){
                            System.out.println("要更新的数据"+Integer.parseInt(scoreTv.getText().toString()));
                            System.out.println("现在数据库里的数据"+queryScore(name,theme));
                            updateData(name,Integer.parseInt(scoreTv.getText().toString()));
                            Toast.makeText(IndexActivity.this, "更新记录中…" , Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent();
                        intent.setClass(IndexActivity.this, MainActivity.class);
                        startActivity(intent);
                        //
                        Intent intent2 = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("name",name);
                        bundle2.putString("theme", theme);
                        bundle2.putSerializable("card",card);
                        intent2.putExtras(bundle2) ;
                        intent2.setClass(IndexActivity.this,MainActivity.class);

                    }
                });

                //    设置一个NegativeButton
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(IndexActivity.this, "继续游戏" , Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.show();
            }
        });
        reGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });

    }

    private void initDB() {
        helper = new MyHelper(IndexActivity.this);
        mydb = helper.getWritableDatabase();
    }

    private void initialize(){
        myHelper = new MyHelper(this);
        scoreTv = findViewById(R.id.ScoreTv);
        userTv = findViewById(R.id.UserTv);
        backBtn = findViewById(R.id.BackBtn);
        reGameBtn = findViewById(R.id.RegameBtn);
        textView10 = (TextView)findViewById(R.id.textView10) ;
        numDouble = new int[4][4];
        num = new int[16];
        score = 0;
        randomNumberAndContinue();
        transNumber();
        setTvs((ViewGroup) findViewById(R.id.relativeLayout));
    }

    //////////数据库操作//////////
    private void insertData(String id, String theme, int score) {//插入数据
        String sql = "INSERT INTO " + helper.TABLE_NAME + " (id,theme,score,time) VALUES ('" + id + "','" + theme + "'," + score + ",'2020-06-25')";
        mydb.execSQL(sql);
    }

    private void updateData(String id, int score) {//更新分数
        String sql = "UPDATE " + helper.TABLE_NAME + " SET score=" + score + " WHERE id='" + id +"' AND theme = '"+theme +"'";
        mydb.execSQL(sql);
    }

    private void removeData(String id) {//删除数据
        String sql = "DELETE FROM " + helper.TABLE_NAME + " WHERE id='" + id+"'";
        mydb.execSQL(sql);
    }

    private int queryScore(String id, String theme) {//获取当前的分数（最高分）
        String sql = "SELECT score FROM "+ helper.TABLE_NAME + " WHERE id='" + id +"' AND theme = '"+theme +"'";
        Cursor cursor = mydb.rawQuery(sql, null);
        String result = null;int score = 0;
        if (cursor.getCount() == 0) {
            result = "无数据";
            return 0;
        }
        else {
            result = "";
            while (cursor.moveToNext()) {
                score = cursor.getInt(0);
            }
            return score;
        }
    }
    private void queryData() {//获取所有信息
        String sql = "SELECT * FROM " + helper.TABLE_NAME;
        Cursor cursor = mydb.rawQuery(sql, null);
        String result = null;
        if (cursor.getCount() == 0) {
            result = "无数据";
        }
        else {
            result = "";
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String theme = cursor.getString(1);
                int score = cursor.getInt(2);
                String finalscore = score+"";
                String temp = id + ", " + theme + ", " + finalscore + "分\n";
                result += temp;
            }
        }
    }

    ////////2048/////////
    private String changeText(int i){
        int log= (int) Math.log(i)/(int) Math.log(2);
        return card[log-1];
    }

    private void ViewGroupOnTouchEvent(ViewGroup viewGroup){
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            float x1 =0;
            float y1 =0;
            float x2 =0;
            float y2 =0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if(y1 - y2 > 50 &&  x1-x2<y1-y2 && x2-x1<y1-y2) {
                        swipeUp();
                        flash();
                    } else if(y2 - y1 > 50 &&  x1-x2<y2-y1 && x2-x1<y2-y1) {
                        swipeDown();
                        flash();
                    } else if(x1 - x2 > 50 ) {
                        swipeLeft();
                        flash();
                    } else if(x2 - x1 > 50) {
                        swipeRight();
                        flash();
                    }
                }
                return true;
            }
        });
    }

    private void flash(){
        scoreTv.setText(score+"");
        if (randomNumberAndContinue()){
            transNumber();
            setTvs((ViewGroup) findViewById(R.id.relativeLayout));
        }else{
            Toast.makeText(IndexActivity.this,"游戏结束，得分"+score,Toast.LENGTH_SHORT).show();
        }
    }


    public void transNumber(){
        int index = 0;
        for(int i=0;i<numDouble.length;i++){//控制行数
            for(int j=0;j<numDouble[i].length;j++){//一行中有多少个元素（即多少列）
                num[index++]=numDouble[j][i];           //有一点点问题——————————————？
            }
        }
    }


    private boolean randomNumberAndContinue(){
        for(int n :num){
            if(n==0){
                while(true){
                    Random random = new Random();
                    int i = random.nextInt(4);
                    int j = random.nextInt(4);
                    if(numDouble[i][j] == 0){
                        numDouble[i][j] = random.nextInt(4)+1 == 4 ? 4 : 2;
                        return true;
                    }
                }
            }
        }

        for(int i =0 ;i<numDouble.length;i++){
            for (int j =0 ;j<numDouble[i].length;j++){
                if(i!=numDouble.length-1){
                    if(numDouble[i][j] == numDouble[i+1][j]&&numDouble[i][j]!=(int)Math.pow(2,card.length)){
                        return true;
                    }
                }
                if(j!=numDouble[i].length-1){
                    if(numDouble[i][j] == numDouble[i][j+1]&&numDouble[i][j]!=(int)Math.pow(2,card.length)){
                        return true;
                    }
                }
            }
        }
        for(int i=0;i<numDouble.length;i++){
            for(int j=0;j<numDouble.length;j++){
                if(numDouble[i][j]==0)
                    return true;
            }
        }
        return false;

    }

    public static double log(int basement, int n){
        return Math.log(n) / Math.log(basement);
    }
    private void setTvs(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int j = 0;
            if (view instanceof TextView) {
                TextView numTv = (TextView)view;
                if(num[i]>0&&num[i]<=(int)Math.pow(2,card.length)){
                    int log= (int) log(2,num[i]);
                    int k=log-1;
                    change=card[k];
                }

                switch (num[i]) {
                    case 0:
                        numTv.setBackgroundColor(0x33ffffff);
                        break;
                    case 2:
                        numTv.setBackgroundColor(0xffeee4da);
                        break;
                    case 4:
                        numTv.setBackgroundColor(0xffede0c8);
                        break;
                    case 8:
                        numTv.setBackgroundColor(0xfff2b179);
                        break;
                    case 16:
                        numTv.setBackgroundColor(0xfff59563);
                        break;
                    case 32:
                        numTv.setBackgroundColor(0xfff67c5f);
                        break;
                    case 64:
                        numTv.setBackgroundColor(0xfff65e3b);
                        break;
                    case 128:
                        numTv.setBackgroundColor(0xffedcf72);
                        break;
                    case 256:
                        numTv.setBackgroundColor(0xffedcc61);
                        break;
                    case 512:
                        numTv.setBackgroundColor(0xffedc850);
                        break;
                    case 1024:
                        numTv.setBackgroundColor(0xffedc53f);
                        break;
                    case 2048:
                        numTv.setBackgroundColor(0xffedc22e);
                        break;
                }
                if (num[i] <= 0){
                    numTv.setText("");
                }else {
                    numTv.setText(change+"");
                }
            }
        }
    }


    private boolean noBlockHorizontal(int row,int col,int k,int[][]card){
        int n,m;
        if(col>=k){
            n=k;
            m=col;
        }else{
            n=col;
            m=k;
        }
        for(int i=n+1;i<m;i++){
            if(card[i][row]!=0){
                return false;
            }
        }
        return true;
    }

    private boolean noBlockVertical(int col,int row,int k,int[][]card){
        int n,m;
        if(row>=k){
            n=k;
            m=row;
        }else{
            n=row;
            m=k;
        }
        for(int i=n+1;i<m;i++){
            if(card[col][i]!=0){
                return false;
            }
        }
        return true;
    }
    private void swipeLeft() { // 往左滑动手指
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (numDouble[x1][y] > 0) {
                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x1][y];
                            numDouble[x1][y]=0;
                            x--;
                            break;
                        } else if(numDouble[x][y]==numDouble[x1][y]&&noBlockHorizontal(y,x,x1,numDouble)&&numDouble[x][y]*2<=(int)Math.pow(2,card.length)){//横轴变
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x1][y]=0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeRight() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >=0; x--) {
                for (int x1 = x - 1; x1 >=0; x1--) {
                    if (numDouble[x1][y] > 0) {
                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x1][y];
                            numDouble[x1][y]=0;
                            x++;
                            break;
                        } else if(numDouble[x][y]==numDouble[x1][y]&&noBlockHorizontal(y,x,x1,numDouble)&&numDouble[x][y]*2<=(int)Math.pow(2,card.length)){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x1][y]=0;
                            break;
                        }
                    }
                }
            }
        }


    }

    private void swipeUp() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (numDouble[x][y1] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x][y1];
                            numDouble[x][y1]=0;
                            y--;
                            break;
                        } else if(numDouble[x][y]==numDouble[x][y1]&&noBlockVertical(x,y,y1,numDouble)&&numDouble[x][y]*2<=(int)Math.pow(2,card.length)){//纵轴变
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=(numDouble[x][y]*2);
                            numDouble[x][y1]=0;
                            break;
                        }
                    }
                }
            }
        }

    }

    private void swipeDown() {

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >=0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (numDouble[x][y1] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x][y1];
                            numDouble[x][y1]=0;
                            y++;
                            break;
                        } else if(numDouble[x][y]==numDouble[x][y1]&&noBlockVertical(x,y,y1,numDouble)&&numDouble[x][y]*2<=(int)Math.pow(2,card.length)){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x][y1]=0;
                            break;
                        }
                    }
                }
            }
        }
    }
}
