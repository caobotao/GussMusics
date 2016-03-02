package com.cbt.guessmusic.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.model.IDialogButtonListener;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by caobotao on 16/2/28.
 */
public class Util {
    private static Util util;
    private static AlertDialog mAlertDialog;

    private Util() {
    }

    public static Util getInstance() {
        if (util == null) {
            synchronized (Util.class) {
                if (util == null) {
                    util = new Util();
                }
            }
        }
        return util;
    }

    /**
     * 显示自定义对话框
     */
    public void showDialog(Context context,boolean isOkDialog, String message, final IDialogButtonListener listener) {
        //自定义对话框布局
        View dialogView = getView(context, R.layout.warning_dialog);
        Builder builder = new Builder(context);
        TextView dialogContent = (TextView) dialogView.findViewById(R.id.dialog_content);
        dialogContent.setText(message);

        if (!isOkDialog) {
            LinearLayout okButtonLayout = (LinearLayout) dialogView.findViewById(R.id.ok_button_layout);
            okButtonLayout.setVisibility(View.INVISIBLE);

            ImageButton yesButton = (ImageButton) dialogView.findViewById(R.id.yes_button);
            ImageButton noButton = (ImageButton) dialogView.findViewById(R.id.no_button);

            yesButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏对话框
                    if (mAlertDialog != null) {
                        mAlertDialog.cancel();
                    }
                    //执行点击"是"按钮时的回调方法
                    if (listener != null) {
                        listener.onYesButtonClick();
                    }
                }
            });
            noButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏对话框
                    if (mAlertDialog != null) {
                        mAlertDialog.cancel();
                    }
                }
            });
        } else {
            ImageButton okButton = (ImageButton) dialogView.findViewById(R.id.ok_button);
            LinearLayout okButtonLayout = (LinearLayout) dialogView.findViewById(R.id.ok_button_layout);
            okButtonLayout.setVisibility(View.VISIBLE);
            LinearLayout yesAndNoButtonLayout = (LinearLayout) dialogView.findViewById(R.id.yes_and_no_button_layout);
            yesAndNoButtonLayout.setVisibility(View.GONE);

            okButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏对话框
                    if (mAlertDialog != null) {
                        mAlertDialog.cancel();
                    }
                }
            });
        }
        //设置View
        builder.setView(dialogView);
        //创建并显示对话框
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    //跳转至另一个Activity
    public void startActivityWithoutData(Context context, Class dest) {
        context.startActivity(new Intent(context, dest));
        //关闭当前Activity
        ((Activity)context).finish();
    }

    //根据布局id获取对应View
    public View getView(Context context, int layoutId) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, null);
        return view;
    }

    //获取一个随机的汉字字符
    public char getRandomChineseChar() {
        String str = "";
        int highPos;
        int lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));
        byte[] bytes = new byte[2];
        bytes[0] = Integer.valueOf(highPos).byteValue();
        bytes[1] = Integer.valueOf(lowPos).byteValue();
        try {
            str = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }
}
