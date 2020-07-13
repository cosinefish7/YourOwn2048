package cn.edu.slidetest2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

public class infodialog extends Dialog {
    private EditText editTextName1,editTextName2,editTextName3;
    private Spinner spinner1;
    public static String[] options = {"经典模式","字母表","10以内数字","自定义"};
    public static String[] cards = {};
    public static String[][] opt_cards = {};
    private boolean isExist = false;
    private themehelper helper;
    private SQLiteDatabase mydb;

    public infodialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_info);//输出自定义对话框布局

/**总结自定义对话框步骤：
 * ①设计自定义对话框布局样式--》dialog_layout.xml
 * ②设计style文件（关闭自定义对话框的标题、去除背景（把背景设置成透明色））
 *③ 将第一步的布局应用到当前自定义对话框（mydialog.java中设置，另外也要实现YES按钮和NO按钮的响应功能）
 * ④在MainActivity.java实例化对话框（参数1：环境上下文（默认设置：this），参数2：导入样式R.style/样式名     ；并且show（）方法展示出对话框效果）
 */

        editTextName1 = (EditText)findViewById(R.id.editTextName1);
        editTextName2 = (EditText)findViewById(R.id.editTextName2);
        editTextName3 = (EditText)findViewById(R.id.editTextName3);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        helper = new themehelper(getContext());
        mydb = helper.getWritableDatabase();

/////////////////////////////////////////////////////

        spinner1 = (Spinner)findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),R.layout.myspinner,options);
        spinner1.setAdapter(adapter);

        Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 选择选项后作出反应的代码
                if (position==0){
                    editTextName2.setText("经典模式");
                    editTextName3.setText("1，2，4，8，16，32，64，128，256，512，1024，2048，4096，8192");
                }
                else if (position == 1){
                    editTextName2.setText("字母表");
                    editTextName3.setText("A，B，C，D，E，F，G，H，I，J，K，L，M，N，O，P，Q，R，S，T，U，V，W，X，Y，Z");
                }
                else if (position == 2){
                    editTextName2.setText("10以内数字");
                    editTextName3.setText("1，2，3，4，5，6，7，8，9");
                }
                else if (position == 3){
                    editTextName2.setText("请输入自定义主题~");
                    editTextName3.setText("自定义的内容~");
                }
                else{
                    editTextName2.setText(options[position]);
                    editTextName3.setText(cards[position-4]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //还没有选择主题哦
            }
        };
        spinner1.setOnItemSelectedListener(listener);

        editTextName1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editTextName1.setSelectAllOnFocus(true);
                editTextName1.selectAll();
                return false;
            }
        });
        editTextName2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editTextName2.setSelectAllOnFocus(true);
                editTextName2.selectAll();
                return false;
            }
        });
        editTextName3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                editTextName3.setSelectAllOnFocus(true);
                editTextName3.selectAll();
                return false;
            }
        });

        findViewById(R.id.nobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();  //自定义对话框点击NO按钮时，取消
            }
        });

        //YES按钮安装监听器，实现响应功能
        findViewById(R.id.yesbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Bundle bundle2 = new Bundle();
                String inputName = editTextName1.getText().toString();
                String inputTheme = editTextName2.getText().toString();
                String str=editTextName3.getText().toString();
                String[ ] inputCard = str.split("，");
                bundle.putString("name", inputName);
                bundle.putString("theme", inputTheme);
                bundle.putSerializable("card", inputCard);
                //insertData(inputTheme,str);

                int i = 0;
                for(i = 0;i<options.length;i++){
                    if (options[i].equals(inputTheme)){
                        isExist = true;
                    }
                }
                if (isExist == false){
                    String[] array=new String[options.length+1]; //创建一个新数组
                    for(int ii=0;ii<options.length;ii++) {
                        array[ii]=options[ii];
                    }
                    //将新元素添加到新数组
                    array[options.length]=inputTheme;
                    options=array;

                    String[] array2=new String[cards.length+1]; //创建一个新数组
                    for(int ii=0;ii<cards.length;ii++) {
                        array2[ii]=cards[ii];
                    }
                    //将新元素添加到新数组
                    array2[cards.length]=str;
                    cards=array2;
                }



                if (inputName.equals("")) {
                    //Toast.makeText(, "请务必留下大名哦！" + inputName, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtras(bundle) ;
                    intent.setClass(getContext(),IndexActivity.class);
                    getContext().startActivity(intent);
                    ///
                    Intent intent2 = new Intent();
                    intent2.putExtras(bundle2) ;
                    intent2.setClass(getContext(),MainActivity.class);
                }
            }
        });
    }


//    private void insertData(String theme, String cards) {//插入数据
//        String sql = "INSERT INTO " + helper.TABLE_NAME + " (theme,cards) VALUES ('" + theme + "','" + cards + "')";
//        mydb.execSQL(sql);
//    }

//    private String querycards(String theme) {
//        String sql = "SELECT cards FROM "+ helper.TABLE_NAME + " WHERE theme = '"+theme +"'";
//        Cursor cursor = mydb.rawQuery(sql, null);
//        String result = null;String cards = null;
//        if (cursor.getCount() == 0) {
//            result = "无数据";
//            return "无";
//        }
//        else {
//            result = "";
//            while (cursor.moveToNext()) {
//                cards = cursor.getString(0);
//            }
//            return cards;
//        }
//    }
}
