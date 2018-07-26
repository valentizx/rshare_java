package me.rex.sdk.line;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import me.rex.sdk.RShare;
import me.rex.sdk.RSharePlatform;
import me.rex.sdk.util.RFileHelper;
import me.rex.sdk.util.RPlatformHelper;

final public class RLineManager extends RShare {
    private final String TAG = "RInstagramManager===>";

    private static RLineManager mManager;

    private RLineManager(){}
    public static RLineManager getInstance(){
        if (mManager == null) {
            synchronized (RLineManager.class) {
                if (mManager == null) {
                    mManager = new RLineManager();
                }
            }
        }
        return mManager;
    }

    public void shareText(Context context, String text) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Line)) {
            Log.e(TAG, "Line 未安装");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/*");

        intent.setPackage("jp.naver.line.android");
        context.startActivity(intent);

    }
    public void shareImage(Context context, Bitmap image) {
        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Line)) {
            Log.e(TAG, "Line 未安装");
            return;
        }
        RFileHelper.detectFileUriExposure();
        RFileHelper.saveBitmapToExternalSharePath(context, image);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        Uri uri = RFileHelper.getExternalSharePathFileUris(context).get(0);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.setPackage("jp.naver.line.android");
        context.startActivity(intent);

    }

}
