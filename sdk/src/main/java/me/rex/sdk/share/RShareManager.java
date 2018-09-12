package me.rex.sdk.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import me.rex.sdk.facebook.RFacebookManager;
import me.rex.sdk.googleplus.RGooglePlusManager;
import me.rex.sdk.instagram.RInstagramManager;
import me.rex.sdk.line.RLineManager;
import me.rex.sdk.pinterest.RPinterestManager;
import me.rex.sdk.qq.RQqManager;
import me.rex.sdk.sina.RSinaWeiboManager;
import me.rex.sdk.tumblr.RTumblrManager;
import me.rex.sdk.twitter.RTwitterManager;
import me.rex.sdk.wechat.RWechatManager;
import me.rex.sdk.whatsapp.RWhatsAppManager;

public final class RShareManager {

    public enum ShareChannel {
        QQSession,
        QQFavorite,
        QQDataLine,
        QZone,
        WechatSession,
        WechatFavorite,
        WechatTimeline,
        FacebookClient,
        FacebookBroswer,
        TwitterInnerApp,
        TwitterClient,
        SinaWeibo,
        SinaWeiboStory,
        Line,
        Instagram,
        Tumblr,
        Pinterest,
        GooglePlus,
        WhatsApp
    }

    private final String TAG = "RShareManager==>";


    private static RShareManager mManager;

    private RShareManager(){}
    public static RShareManager getInstance(){
        if (mManager == null) {
            synchronized (RShareManager.class) {
                if (mManager == null) {
                    mManager = new RShareManager();
                }
            }
        }
        return mManager;
    }


    public void shareImage(Context context,
                           RImageContent content,
                           ShareChannel channel,
                           RShareListener listener) {
        ArrayList<Bitmap> img = new ArrayList<Bitmap>();
        img.add(content.getImage());

        if (channel == ShareChannel.QQSession || channel == ShareChannel.QQFavorite || channel ==
                ShareChannel.QQDataLine
                ) {
            RQqManager.getInstance().shareImage(context, content.getImage(),listener);
        } else if(channel == ShareChannel.QZone) {
            RQqManager.getInstance().publishImagesToZone(context, img, content.getQuote(), listener);
        } else if (channel == ShareChannel.WechatSession) {
            RWechatManager.getInstance().shareImage(context, content.getImage(), RWechatManager
                    .TargetScene.Session, listener);
        } else if (channel == ShareChannel.WechatFavorite) {
            RWechatManager.getInstance().shareImage(context, content.getImage(), RWechatManager
                    .TargetScene.Favorite, listener);
        } else if (channel == ShareChannel.WechatTimeline) {
            RWechatManager.getInstance().shareImage(context, content.getImage(), RWechatManager
                    .TargetScene.Timeline, listener);
        } else if (channel == ShareChannel.FacebookClient) {
            RFacebookManager.getInstance().sharePhoto(context, img);
        } else if (channel == ShareChannel.TwitterInnerApp) {
            RTwitterManager.getInstance().share(context, null, content.getQuote(), content
                    .getImage(), null, RShare.Mode.Automatic, listener);
        } else if (channel == ShareChannel.TwitterClient) {
            RTwitterManager.getInstance().share(context, null, content.getQuote(), content
                    .getImage(), null, RShare.Mode.Native, listener);
        } else if (channel == ShareChannel.SinaWeibo) {
            RSinaWeiboManager.getInstance().sharePhoto(context, img, content.getQuote(), false,
                    listener);
        } else if (channel == ShareChannel.SinaWeiboStory) {
            RSinaWeiboManager.getInstance().sharePhoto(context, img, content.getQuote(), true, listener);
        } else if (channel == ShareChannel.Line) {
            RLineManager.getInstance().shareImage(context, content.getImage());
        } else if (channel == ShareChannel.Instagram) {
            RInstagramManager.getInstance().shareImage(context, content.getImage());
        } else if (channel == ShareChannel.Tumblr) {
            if (!content.getImageUrl().isEmpty()) {
                RTumblrManager.getInstance().shareImage(context, content.getImageUrl(), content
                        .getQuote(), null, listener);
            }

        } else if (channel == ShareChannel.Pinterest) {
            if (!content.getImageUrl().isEmpty()) {
                RPinterestManager.getInstance().shareImage(context, content.getImageUrl());
            }
        } else if (channel == ShareChannel.WhatsApp) {
            RWhatsAppManager.getInstance().share(context, content.getImage(), content.getQuote());
        } else {
            Log.e(TAG , "该种方式不支持图片分享");
        }
    }
    public void shareText(Context context,
                           RTextContent content,
                           ShareChannel channel,
                           RShareListener listener) {
        if (channel == ShareChannel.WechatSession) {
            RWechatManager.getInstance().shareText(context, content.getBody(), RWechatManager
                    .TargetScene.Session, listener);
        } else if (channel == ShareChannel.WechatFavorite) {
            RWechatManager.getInstance().shareText(context, content.getBody(), RWechatManager
                    .TargetScene.Favorite, listener);
        } else if (channel == ShareChannel.WechatTimeline) {
            RWechatManager.getInstance().shareText(context, content.getBody(), RWechatManager
                    .TargetScene.Timeline, listener);
        } else if (channel == ShareChannel.TwitterInnerApp) {
            RTwitterManager.getInstance().share(context, null, content.getBody(), null, null,
                    RShare.Mode.Automatic, listener);
        } else if (channel == ShareChannel.TwitterClient) {
            RTwitterManager.getInstance().share(context, null, content.getBody(), null, null,
                    RShare.Mode.Native, listener);
        } else if (channel == ShareChannel.SinaWeibo) {
            RSinaWeiboManager.getInstance().shareText(context, content.getBody(), listener);
        } else if (channel == ShareChannel.Line) {
            RLineManager.getInstance().shareText(context, content.getBody());
        } else if (channel == ShareChannel.Tumblr) {
            RTumblrManager.getInstance().shareText(context, content.getBody(), content
                    .getTitle(), content.getWebpageUrl(), listener);
        } else if (channel == ShareChannel.WhatsApp) {
            RWhatsAppManager.getInstance().share(context, null, content.getBody());
        } else if (channel == ShareChannel.GooglePlus) {
            RGooglePlusManager.getInstance().share(context, content.getWebpageUrl(), content.getBody());
        } else {
            Log.e(TAG , "该种方式不支持文字分享");
        }

    }
    public void shareVideo(Context context,
                           RVideoContent content,
                           ShareChannel channel,
                           RShareListener listener) {
        if (channel == ShareChannel.QZone) {
            RQqManager.getInstance().publishVideoToZone(context, content.getLocalVideoUrl(),
                    content.getQuote(), listener);
        } else if (channel == ShareChannel.WechatSession) {
            RWechatManager.getInstance().shareFile(context, content.getLocalVideoUrl(), content
                    .getTitle(), content.getThumbImage(), RWechatManager.TargetScene.Session,
                    listener);
        } else if (channel == ShareChannel.WechatFavorite) {
            RWechatManager.getInstance().shareFile(context, content.getLocalVideoUrl(), content
                            .getTitle(), content.getThumbImage(), RWechatManager.TargetScene.Favorite,
                    listener);
        } else if (channel == ShareChannel.FacebookClient) {
            RFacebookManager.getInstance().shareLocalVideo(context, content.getLocalVideoUrl());
        } else if (channel == ShareChannel.SinaWeibo) {
            RSinaWeiboManager.getInstance().shareLocalVideo(context, content.getLocalVideoUrl(),
                    content.getQuote(), false, listener);
        } else if (channel == ShareChannel.SinaWeiboStory) {
            RSinaWeiboManager.getInstance().shareLocalVideo(context, content.getLocalVideoUrl(),
                    content.getQuote(), true, listener);
        } else if (channel == ShareChannel.Instagram) {
            RInstagramManager.getInstance().shareVideo(context, content.getLocalVideoUrl());
        } else {
            Log.e(TAG , "该种方式不支持视频分享");
        }

    }
    public void shareWebpage(Context context,
                           RWebpageContent content,
                           ShareChannel channel,
                           RShareListener listener) {
        if (channel == ShareChannel.QQSession || channel == ShareChannel.QQFavorite || channel ==
                ShareChannel.QQDataLine
                ) {
            RQqManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getTitle(), content.getQuote(), content.getThumbImageUrl(), listener);
        } else if(channel == ShareChannel.QZone) {
            ArrayList<String>img = new ArrayList<>();
            img.add(content.getThumbImageUrl());
            RQqManager.getInstance().shareWebpageToZone(context, content.getWebpageUrl(), content
                    .getTitle(), content.getQuote(), img ,listener);
        } else if (channel == ShareChannel.WechatSession) {
            RWechatManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getTitle(), content.getQuote(), content.getThumbImage(), RWechatManager
                    .TargetScene.Session, listener);
        } else if (channel == ShareChannel.WechatFavorite) {
            RWechatManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getTitle(), content.getQuote(), content.getThumbImage(), RWechatManager
                    .TargetScene.Favorite, listener);
        } else if (channel == ShareChannel.WechatTimeline) {
            RWechatManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getTitle(), content.getQuote(), content.getThumbImage(), RWechatManager
                    .TargetScene.Timeline, listener);
        } else if (channel == ShareChannel.FacebookClient) {
            RFacebookManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getQuote(), content.getHashTag(), RShare.Mode.Automatic, listener);
        } else if (channel == ShareChannel.FacebookBroswer) {
            RFacebookManager.getInstance().shareWebpage(context, content.getWebpageUrl(), content
                    .getQuote(), content.getHashTag(), RShare.Mode.Feed, listener);
        } else if (channel == ShareChannel
                .TwitterInnerApp) {
            RTwitterManager.getInstance().share(context, content.getWebpageUrl(), content.getQuote(), content
                    .getThumbImage(), content.getHashTag(), RShare.Mode.Automatic, listener);
        } else if (channel == ShareChannel.TwitterClient) {
            RTwitterManager.getInstance().share(context, content.getWebpageUrl(), content.getQuote(), content
                    .getThumbImage(), null, RShare.Mode.Native, listener);
        } else if (channel == ShareChannel.SinaWeibo) {
            RSinaWeiboManager.getInstance().shareWebpage(context, content.getWebpageUrl(),
                    content.getTitle(), content.getQuote(), content.getThumbImage(), listener);
        } else if (channel == ShareChannel.Tumblr) {
            if (!content.getThumbImageUrl().isEmpty()) {
                RTumblrManager.getInstance().shareImage(context, content.getThumbImageUrl(), content
                        .getQuote(), content.getWebpageUrl(), listener);
            }

        } else if (channel == ShareChannel.WhatsApp) {
            RWhatsAppManager.getInstance().share(context, content.getThumbImage(), content.getWebpageUrl());
        } else  if(channel == ShareChannel.GooglePlus) {
            RGooglePlusManager.getInstance().share(context, content.getWebpageUrl(), content.getQuote());
        } else {
            Log.e(TAG , "该种方式不支持图片分享");
        }

    }


}
