package me.rex.sdk.googleplus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.plus.PlusShare;

import me.rex.sdk.RShare;
/**
 * Google + <a href=https://developers.google.com/+/mobile/android/share/>Android 版</a>
 * */
final public class RGooglePlusManager extends RShare {

    private static RGooglePlusManager mManager;


    private RGooglePlusManager(){}
    public static RGooglePlusManager getInstance(){
        if (mManager == null) {
            synchronized (RGooglePlusManager.class) {
                if (mManager == null) {
                    mManager = new RGooglePlusManager();
                }
            }
        }
        return mManager;
    }

    /**
     * 分享链接、文字至 Google +.
     * @param context 上下文.
     * @param webpageUrl 网页链接.
     * @param text 内容.
     */
    public void share(@NonNull Context context, @Nullable String webpageUrl,@NonNull String text ) {
        /**/
        Intent shareIntent = new PlusShare.Builder(context)
                .setType("text/plain")
                .setText(text)
                .setContentUrl(Uri.parse(webpageUrl == null ? "" : webpageUrl))
                .getIntent();

        context.startActivity(shareIntent);
    }
}
