package me.rex.sdk.tumblr;

import android.content.Intent;

import com.flurry.android.tumblr.PhotoPost;
import com.flurry.android.tumblr.TextPost;

final public class RTumblrHelper {


    private Intent mIntent;

    public RTumblrHelper (Intent intent) {
        mIntent = intent;
    }


    protected PhotoPost getImageParam() {
        PhotoPost param = new PhotoPost(mIntent.getStringExtra("imageUrl"));
        param.setCaption(mIntent.getStringExtra("description"));
        param.setWebLink(mIntent.getStringExtra("webpageUrl"));
        return param;
    }

    protected TextPost getTextParam() {
        TextPost param = new TextPost(mIntent.getStringExtra("body"));
        param.setTitle(mIntent.getStringExtra("title"));
        param.setWebLink(mIntent.getStringExtra("webpageUrl"));
        return param;
    }
}
