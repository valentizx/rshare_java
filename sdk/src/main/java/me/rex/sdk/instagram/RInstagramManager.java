package me.rex.sdk.instagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;


import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;

/**
 * Instagram <a href=https://www.instagram.com/developer/mobile-sharing/android-intents/>Instagram
 * Android分享</a>
 * */
final public class RInstagramManager extends RShare {

    private final String TAG = "RInstagramManager===>";

    private static RInstagramManager mManager;

    private RInstagramManager(){}
    public static RInstagramManager getInstance(){
        if (mManager == null) {
            synchronized (RInstagramManager.class) {
                if (mManager == null) {
                    mManager = new RInstagramManager();
                }
            }
        }
        return mManager;
    }

    public void shareVideo(Context context, Uri localVideoUrl ) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Instagram)) {
            Log.e(TAG, "Instagram 未安装");
            return;
        }

        RFileHelper.detectFileUriExposure();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_STREAM, localVideoUrl);
        intent.setPackage("com.instagram.android");
        context.startActivity(intent);
    }

    public void shareImage(Context context, Bitmap image) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Instagram)) {
            Log.e(TAG, "Instagram 未安装");
            return;
        }
        RFileHelper.deleteExternalShareDirectory(context);
        RFileHelper.saveBitmapToExternalSharePath(context, image);
//        /**
//         *
//         * 系统分享.
//         * **/
//        if (mode == Mode.System) {
//
//            Uri imageUri;
//            /**
//             *
//             * Android 7.0 以后更改了获取文件的 Uri 策略, 使用 FileProvider 来获取文件的 Uri.
//             * **/
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                String authority = context.getPackageName() + ".fileprovider";
//                imageUri = FileProvider.getUriForFile(
//                        context,
//                        authority,
//                        RFileHelper.getExternalSharePathFiles(context).get(0));
//            } else {
//
//                /**
//                 * Android 7.0 之前获取文件 Uri 的方法.
//                 * **/
//                imageUri = RFileHelper.getExternalSharePathFileUris(context).get(0);
//            }
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("image/jpeg");
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.putExtra(Intent.EXTRA_STREAM, imageUri );
//            context.startActivity(Intent.createChooser(intent, "分享"));
//
//
//        } else {

        RFileHelper.detectFileUriExposure();
        /**
         *
         * 直接启动 Instagram 客户端分享.
         * */
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        Uri uri = RFileHelper.getExternalSharePathFileUris(context).get(0);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setPackage("com.instagram.android");
        context.startActivity(intent);
       // }
    }
}
