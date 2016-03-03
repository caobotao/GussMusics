package com.cbt.guessmusic.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.SendMessageToWX.Req;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;
/**
 * Created by caobotao on 16/3/3.
 */
public class WeiXinUtil {
    public static final String APP_ID = "wx0b4ecb0f974c5a4b";
    private static final int THUMB_SIZE = 150;

    private IWXAPI mApi;
    private Context mContext;
    private static WeiXinUtil mInstance;

    private WeiXinUtil(Context context) {
        mContext = context;
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, false);
        mApi.registerApp(APP_ID);
    }

    public static WeiXinUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WeiXinUtil.class) {
                if (mInstance == null) {
                    mInstance = new WeiXinUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送文本信息到微信
     */
    public void sendRequest(String text) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "txt" + String.valueOf(System.currentTimeMillis());
        req.message = msg;

        //Req.WXSceneTimeline: 发送至微信的会话内
        //Req.WXSceneSession: 发送至微信朋友圈
        req.scene = Req.WXSceneTimeline;
        mApi.sendReq(req);
    }

    /**
     * 发送图片到微信
     */
    public void sendBitmap(Bitmap bitmap) {
        WXImageObject imageObject = new WXImageObject();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img" + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = Req.WXSceneTimeline;

        mApi.sendReq(req);
    }
}
