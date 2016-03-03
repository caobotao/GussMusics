package com.cbt.guessmusic.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbt.guessmusic.R;
import com.cbt.guessmusic.data.Const;
import com.cbt.guessmusic.model.IDialogButtonListener;


import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by caobotao on 16/2/28.
 */
public class Util {
    private static Util util;
    private static AlertDialog mAlertDialog;
    private static final String TAG = "Util";
    private static final String GAME_DATA_SEPARATOR = ":";
    private static final int STAGE_INDEX_DECODED_STR = 0;
    private static final int COINS_DECODED_STR = 1;

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
    public void showDialog(final Context context, boolean isOkDialog, String message, final IDialogButtonListener listener) {
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
                        //点击"是"按钮时的音效
                        MusicPlayerUtil.playTone(context,MusicPlayerUtil.INDEX_ENTER);
                    }
                }
            });
            noButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //隐藏对话框
                    if (mAlertDialog != null) {
                        mAlertDialog.cancel();
                        //点击"否"按钮时的音效
                        MusicPlayerUtil.playTone(context,MusicPlayerUtil.INDEX_CANCEL);
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
                        //播放按钮音效
                        MusicPlayerUtil.playTone(context,MusicPlayerUtil.INDEX_CANCEL);
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

    /**
     * 跳转至另一个Activity
     */
    public void startActivityWithoutData(Context context, Class dest) {
        context.startActivity(new Intent(context, dest));
        //关闭当前Activity
        ((Activity)context).finish();
    }

    /**
     * 根据布局id获取对应View
     */
    public View getView(Context context, int layoutId) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, null);
        return view;
    }

    /**
     * 获取一个随机的汉字字符
     */
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

    /**
     * 读取游戏数据
     */
    public int[] readGameData(Context context) {
        FileInputStream fis = null;
        int[] gameData = {0,Const.TOTAL_COINS};
        try {
            fis = context.openFileInput(Const.FILE_NAME_DATA);
            DataInputStream dis = new DataInputStream(fis);

            //获取输入流中的字符串
            String data = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = dis.read(buffer)) != -1) {
                baos.write(buffer,0,length);
            }
            data = baos.toString("UTF-8");
            LogUtil.d(TAG,"data",data);

            //解密过的关卡索引和金币数
            String stageIndexDecodedStr = data.split(GAME_DATA_SEPARATOR)[STAGE_INDEX_DECODED_STR];
            String coinsDecodedStr = data.split(GAME_DATA_SEPARATOR)[COINS_DECODED_STR];
            int stageIndexDecoded = Integer.parseInt(decodeUseBase64(stageIndexDecodedStr));
            int coinsDecoded = Integer.parseInt(decodeUseBase64(coinsDecodedStr));

            gameData[Const.INDEX_LOAD_DATA_STAGE] = stageIndexDecoded;
            gameData[Const.INDEX_LOAD_DATA_COINS] = coinsDecoded;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return gameData;
    }

    /**
     * 保存游戏数据
     */
    public void saveGameData(Context context, int stageIndex, int coins) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Const.FILE_NAME_DATA, Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            //通过base64加密过的关卡索引和金币数
            String stageIndexEncoded = encodeUseBase64(stageIndex + "");
            String coinsEncoded = encodeUseBase64(coins + "");

            dos.writeBytes(stageIndexEncoded);
            dos.writeBytes(GAME_DATA_SEPARATOR);
            dos.writeBytes(coinsEncoded);
//            dos.writeInt(stageIndex);
//            dos.writeInt(coins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取应用签名
     */
    public String getSignature(Context context,String pkgname,View view) {
        PackageManager manager;
        PackageInfo packageInfo;
        Signature[] signatures;
        StringBuilder builder;
        String signature = null;
        boolean isEmpty = TextUtils.isEmpty(pkgname);
        manager = context.getPackageManager();
        builder = new StringBuilder();
        if (isEmpty) {
            Toast.makeText(context, "应用程序的包名不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            try {
                /** 通过包管理器获得指定包名包含签名的包信息 **/
                packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
                /******* 通过返回的包信息获得签名数组 *******/
                signatures = packageInfo.signatures;
                /******* 循环遍历签名数组拼接应用签名 *******/
                for (Signature sign : signatures) {
                    builder.append(sign.toCharsString());
                }
                /************** 得到应用签名 **************/
                signature = builder.toString();
                LogUtil.d("signature","",signature);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return signature;
    }

    /**
     * 将字符串进行Base64编码加密
     */
    public String encodeUseBase64(String string) {
        if (string == null) {
            return null;
        }
        byte[] bytes = Base64.encodeBase64(string.getBytes());
        LogUtil.d(TAG,"encode result",new String(bytes));
        return new String(bytes);
    }

    /**
     * 使用Base64将字符解码
     */
    public String decodeUseBase64(String string) {
        if (string == null) {
            return null;
        }
        byte[] bytes = Base64.decodeBase64(string.getBytes());
        LogUtil.d(TAG,"decode result",new String(bytes));
        return new String(bytes);
    }
}
