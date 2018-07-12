package me.rex.sdk.qq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;

import java.util.ArrayList;

import me.rex.sdk.util.RFileHelper;

final public class RQqHelper {


    private final String TAG = "RQQHelper==>";
    private Intent mIntent;

    public RQqHelper(Intent intent) {
        mIntent = intent;
    }


    protected Bundle getQQWebpageParams() {

        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  mIntent.getStringExtra("description"));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  mIntent.getStringExtra("webpageUrl"));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"));

        Log.e(TAG, "构造参数");
        return params;
    }

    protected Bundle getQQImageParams() {
        Bundle params = new Bundle();
        Context context = RQqManager.getInstance().getContext();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, RFileHelper
                .getExternalSharePathFiles(context).get(0).getAbsolutePath());
        return params;
    }

    protected Bundle getQQMusicParams() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  mIntent.getStringExtra("description"));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mIntent.getStringExtra("targetUrl"));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"));
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, mIntent.getStringExtra("musicUrl"));
        return params;
    }

    protected Bundle getQQAppParams() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"));
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  mIntent.getStringExtra("description"));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  mIntent.getStringExtra("targetUrl"));
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mIntent.getStringExtra("thumbImage"));
        return params;
    }


    protected Bundle getQZoneWebpageParams() {

        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mIntent.getStringExtra("title"));
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  mIntent.getStringExtra("description"));
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  mIntent.getStringExtra("webpageUrl"));

        ArrayList<String> imgs = mIntent.getStringArrayListExtra("images");
        if (imgs != null && imgs.size() != 0) {
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);
        }

        return params;
    }

    protected Bundle getQZoneImageParams() {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, mIntent.getStringExtra("description"));
        ArrayList<String> imgs = mIntent.getStringArrayListExtra ("images");
        for (int i = 0; i < imgs.size(); i ++) {
            Log.e(TAG, imgs.get(i));
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgs);

        return params;
    }

    protected Bundle getQZoneVideoParams() {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish
                .PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO);
        params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, mIntent.getStringExtra("description"));

        params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, mIntent.getStringExtra
                ("local_video_path"));

        return params;
    }
}
