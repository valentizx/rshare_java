package me.rex.sdk.sina;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Utility;

import java.util.ArrayList;

import me.rex.sdk.util.RFileHelper;

final public class RSinaWeiboHelper {


    private final String TAG = "RSinaWeiboHelper";

    private Intent mIntent;


    public RSinaWeiboHelper(Intent intent) {
        mIntent = intent;
    }

    protected WeiboMultiMessage getTextMessage() {

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = getTextObj();
        return message;
    }

    protected WeiboMultiMessage getImageMessage() {

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.textObject = getTextObj();
        message.multiImageObject = getMultiImageObj();
        return message;
    }


    protected WeiboMultiMessage getWebpageMessage() {

        WeiboMultiMessage message = new WeiboMultiMessage();
        message.mediaObject = getWebObj();
        return message;
    }

    protected WeiboMultiMessage getLocalVideoMessage() {
        WeiboMultiMessage message = new WeiboMultiMessage();

        message.textObject = getTextObj();
        message.videoSourceObject = getLocalVideoObj();
        return message;
    }


    protected StoryMessage getPhotoStoryMessage() {

        Context context = RSinaWeiboManager.getInstance().getContext();
        StoryMessage message = new StoryMessage();
        message.setImageUri(RFileHelper.getExternalSharePathFileUris(context).get(0));
        return message;
    }


    protected StoryMessage getVideoStoryMessage() {
        StoryMessage message = new StoryMessage();
        Uri localVideoUri = mIntent.getParcelableExtra("local_video_uri");
        message.setVideoUri(localVideoUri);
        return message;
    }





    // ----------------------------------------//


    /**
     * @return 分享文本实体.
     */
    private TextObject getTextObj() {

        TextObject obj = new TextObject();
        obj.text =  mIntent.getStringExtra("text") == null ? "" :  mIntent.getStringExtra("text");
        obj.title = "share.rex.me"; // 毫无意义
        obj.actionUrl = "http://share.rex.me"; //
        return  obj;
    }

    /**
     * @return 分享图片实体.
     */
    private MultiImageObject getMultiImageObj() {

        Context context = RSinaWeiboManager.getInstance().getContext();
        MultiImageObject obj = new MultiImageObject();

        obj.setImageList(RFileHelper.getExternalSharePathFileUris(context));
        return obj;
    }

    /**
     * @return 分享网页实体.
     */
    private WebpageObject getWebObj() {

        Context context = RSinaWeiboManager.getInstance().getContext();

        WebpageObject obj = new WebpageObject();
        obj.identify = Utility.generateGUID();
        obj.title = mIntent.getStringExtra("title");
        obj.description = mIntent.getStringExtra("description");

        ArrayList <Bitmap> bitmaps = RFileHelper.getExternalSharePathBitmaps(context);
        if (bitmaps != null && bitmaps.size() > 0) {
            obj.setThumbImage(bitmaps.get(0));
        }
        obj.actionUrl = mIntent.getStringExtra("webpage");
        obj.defaultText = "在未设置网页描述下默认提供的文字, 请设置 'description' 字段!";

        return obj;
    }

    /**
     * @return 分享本地视频实体.
     */
    private VideoSourceObject getLocalVideoObj() {

        // Context context = RSinaWeiboManager.getInstance().getContext();
        VideoSourceObject obj = new VideoSourceObject();

        Uri localeVideoUri = mIntent.getParcelableExtra("local_video_uri");
        obj.videoPath = localeVideoUri;
        return obj;


    }

}
