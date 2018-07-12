package me.rex.sdk.wechat;


import android.graphics.Bitmap;
import android.os.health.PackageHealthStats;
import android.util.Log;


import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoFileObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import me.rex.sdk.util.RUtil;

final public class RWechatHelper {

    private final String TAG = "RWechatHelper=====>";

    private SendMessageToWX.Req mReq;
    private WXMediaMessage mMsg;

    private String mMediaTagName = "rex_share_media_tag";
    private String mMessageExt = "rex_share_ext";

    private static final int THUMB_SIZE = 150;

    protected RWechatHelper() {
        mReq = new SendMessageToWX.Req();
        mMsg = new WXMediaMessage();
        mMsg.mediaTagName = mMediaTagName;
        mMsg.messageExt = mMessageExt;
    }


    /**
     * 转换场景: 微信场景 <-> 自定义场景.
     * **/
    private int getScene(RWechatManager.TargetScene scene) {
        if (scene == RWechatManager.TargetScene.Favorite) {
            return SendMessageToWX.Req.WXSceneFavorite;
        } else if (scene == RWechatManager.TargetScene.Session) {
            return SendMessageToWX.Req.WXSceneSession;
        } else {
            return SendMessageToWX.Req.WXSceneTimeline;
        }
    }

/**================构建分享多媒体消息实例 - BEGIN - ================**/

    /**
     * 构建文本分享实例.
     * @param description 文字描述.
     * @return 返回文本分享实例.
     */
    private WXTextObject getTextObj(String description) {

        WXTextObject obj = new WXTextObject();
        obj.text = description;

        return obj;
    }


    /**
     * 构建图片分享实例.
     * @param image Bitmap 格式图片.
     * @return 返回图片分享实例.
     */
    private WXImageObject getImgObj(Bitmap image) {

        WXImageObject obj = new WXImageObject(image);
        return obj;

    }

    /**
     * 构建网页分享实例.
     * @param webpageUrl 网页链接.
     * @return 返回网页分享实例.
     */
    protected WXWebpageObject getWebObj(String webpageUrl) {

        WXWebpageObject obj = new WXWebpageObject();
        obj.webpageUrl = webpageUrl;
        return obj;
    }

    /**
     * 构建音乐分享实例.
     * @param streamUrl 音频流.
     * @param webpageUrl 音乐网页.
     * @return 返回音乐分享实例.
     */
    protected WXMusicObject getMusicObj(String streamUrl, String webpageUrl) {

        WXMusicObject obj = new WXMusicObject();
        obj.musicUrl = webpageUrl;
        obj.musicDataUrl = streamUrl;
        return obj;
    }

    /**
     * 构建视频分享实例.
     * @param videoUrl 视频链接.
     * @return 返回视频分享实例.
     */
    protected WXVideoObject getVideoObj(String videoUrl) {
        WXVideoObject obj = new WXVideoObject();
        obj.videoUrl = videoUrl;
        return obj;

    }

    /**
     * 构建小程序分享实例.
     * @param webpageUrl 小程序网页.
     * @param userName 小程序 Id.
     * @param path 小程序路径.
     * @param type 小程序类型. {@linkplain me.rex.sdk.wechat.RWechatManager.MiniProgramType}.
     * @return 返回小程序分享实例.
     */
    protected WXMiniProgramObject getMiniProgramObj(String webpageUrl, String userName, String
            path, int type) {

        WXMiniProgramObject obj = new WXMiniProgramObject();
        obj.webpageUrl = webpageUrl;
        obj.userName = userName;
        obj.path = path;
        obj.miniprogramType = type;
        return obj;
    }

    protected WXFileObject getFileObj(String path) {
        WXFileObject obj = new WXFileObject();

        obj.setContentLengthLimit(1024 * 1024 * 10);
        obj.setFilePath(path);

        return  obj;
    }

/**================构建分享多媒体消息实例 - END - ================**/



/**================构建分享请求体 - BEGIN - ================**/
    /**
     * 构建文字分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getTextMessageReq(
            String text,
            RWechatManager.TargetScene scene) {

        mMsg.mediaObject = getTextObj(text);
        mMsg.description = text;


        mReq.transaction = buildTransaction("text");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }

    /**
     * 构建图片分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getImgMessageReq(final Bitmap image,
                                                   RWechatManager.TargetScene scene) {

        Bitmap thumbBmp = Bitmap.createScaledBitmap(image, THUMB_SIZE, THUMB_SIZE, true);

        mMsg.mediaObject = getImgObj(image);
        mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);

        mReq.transaction = buildTransaction("img");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }

    /**
     * 构建网页分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getWebMessageReq(String webpageUrl,
                                                   String title,
                                                   String description,
                                                   Bitmap thumbImage,
                                                   RWechatManager.TargetScene scene) {

        mMsg.mediaObject = getWebObj(webpageUrl);
        mMsg.title = title;
        mMsg.description = description;

        if (thumbImage != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUMB_SIZE, THUMB_SIZE, true);
            mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);

        }

        mReq.transaction = buildTransaction("web");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }
    /**
     * 构建音乐分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getMusicMessageReq(String musicUrl, String title, String
            description, Bitmap thumbImage, String webpageUrl, RWechatManager.TargetScene scene) {

        mMsg.mediaObject = getMusicObj(musicUrl, webpageUrl);
        mMsg.title = title;
        mMsg.description = description;

        if (thumbImage != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUMB_SIZE, THUMB_SIZE, true);
            mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);

        }

        mReq.transaction = buildTransaction("music");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }

    /**
     * 构建视频分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getVideoMessageReq(String videoUrl, String title, String
            description, Bitmap thumbImage, RWechatManager.TargetScene scene) {


        mMsg.mediaObject = getVideoObj(videoUrl);
        mMsg.title = title;
        mMsg.description = description;

        if (thumbImage != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUMB_SIZE, THUMB_SIZE, true);
            mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);

        }

        mReq.transaction = buildTransaction("video");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }

    /**
     * 构建小程序分享消息请求体.
     *
     * **/
    protected SendMessageToWX.Req getMiniProgramReq(String userName,
                                                    String path,
                                                    int type,
                                                    String webpageUrl,
                                                    String title,
                                                    String description,
                                                    Bitmap thumbImage,
                                                    RWechatManager.TargetScene scene) {

        mMsg.mediaObject = getMiniProgramObj(webpageUrl, userName, path, type);
        mMsg.title = title;
        mMsg.description = description;

        if (thumbImage != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUMB_SIZE, THUMB_SIZE, true);
            mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);
        }

        mReq.transaction = buildTransaction("miniprogram");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;
    }


    protected SendMessageToWX.Req getFileMessageReq(String path, String title,
                                                    Bitmap thumbImage,
                                                    RWechatManager
                                                    .TargetScene scene) {
        WXFileObject obj = getFileObj(path);

        mMsg.mediaObject = obj;
        mMsg.title = title;


        if (thumbImage != null) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap(thumbImage, THUMB_SIZE, THUMB_SIZE, true);
            mMsg.thumbData = RUtil.bmpToByteArray(thumbBmp, false);
        }
        mReq.transaction = buildTransaction("file");
        mReq.message = mMsg;
        mReq.scene = getScene(scene);
        return mReq;

    }
    /**================构建分享请求体 - END - ================**/




    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
