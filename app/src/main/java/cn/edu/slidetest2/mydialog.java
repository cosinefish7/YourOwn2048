package cn.edu.slidetest2;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

public class mydialog extends Dialog {

    public mydialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_quit);//输出自定义对话框布局

/**总结自定义对话框步骤：
 * ①设计自定义对话框布局样式--》dialog_layout.xml
 * ②设计style文件（关闭自定义对话框的标题、去除背景（把背景设置成透明色））
 *③ 将第一步的布局应用到当前自定义对话框（mydialog.java中设置，另外也要实现YES按钮和NO按钮的响应功能）
 * ④在MainActivity.java实例化对话框（参数1：环境上下文（默认设置：this），参数2：导入样式R.style/样式名     ；并且show（）方法展示出对话框效果）
 */
        //YES按钮安装监听器，实现响应功能

        findViewById(R.id.yesbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);      //自定义对话框点击YES按钮时，执行退出
            }
        });
        //NO按钮安装监听器，实现响应功能
        findViewById(R.id.nobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();  //自定义对话框点击NO按钮时，取消
            }
        });
    }
}