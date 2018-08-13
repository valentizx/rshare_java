package me.rex.sdk.pinterest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RSharePlatform;
import me.rex.sdk.util.RPlatformHelper;


public final class RPinterestManager extends RShare {

    private final String TAG = "RPinterestManager===>";

    private static RPinterestManager mManager;

    private RPinterestManager(){}
    public static RPinterestManager getInstance(){
        if (mManager == null) {
            synchronized (RPinterestManager.class) {
                if (mManager == null) {
                    mManager = new RPinterestManager();
                }
            }
        }
        return mManager;
    }

    public void shareImage(Context context,
                           String imageUrl) {

        if (!RPlatformHelper.isInstalled(context, RSharePlatform.Platform.Pinterest)) {

            Log.e(TAG, "Pinterest 未安装");
            return;
        }


        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setPackage("com.pinterest");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl));

        intent.setType("*/*");
        context.startActivity(intent);
    }
}
