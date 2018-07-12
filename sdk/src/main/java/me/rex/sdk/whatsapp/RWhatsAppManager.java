package me.rex.sdk.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import me.rex.sdk.RShare;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;

final public class RWhatsAppManager extends RShare {

    private final String TAG = "RWhatsAppManager===>";

    private static RWhatsAppManager mManager;

    private RWhatsAppManager(){}
    public static RWhatsAppManager getInstance(){
        if (mManager == null) {
            synchronized (RWhatsAppManager.class) {
                if (mManager == null) {
                    mManager = new RWhatsAppManager();
                }
            }
        }
        return mManager;
    }

    /**
     * 分享图文到 WhatsApp 客户端.
     * @param context 上下文.
     * @param image 图片.
     * @param description 内容描述.
     */
    public void share(@NonNull Context context,
                      @Nullable Bitmap image,
                      @Nullable String description) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.WhatsApp) ) {
            Log.e(TAG, "WhatsApp 未安装.");
            return;
        }
        if (image == null && description == null) {
            Log.e(TAG, "分享参数无效.");
        }

        RFileHelper.detectFileUriExposure();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (description != null) {
            intent.putExtra(Intent.EXTRA_TEXT, description);
            intent.setType("text/plain");
        }

        if (image != null) {
            RFileHelper.saveBitmapToExternalSharePath(context, image);
            intent.putExtra(Intent.EXTRA_STREAM,RFileHelper.getExternalSharePathFileUris(context).get(0));
            intent.setType("image/jpeg");
        }
        intent.setPackage("com.whatsapp");
        context.startActivity(intent);
    }
}
