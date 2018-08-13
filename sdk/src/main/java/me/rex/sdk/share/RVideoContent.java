package me.rex.sdk.share;

import android.graphics.Bitmap;
import android.net.Uri;

public final class RVideoContent {

    private final Uri localVideoUrl;
    private final String videoWebpageUrl;
    private final String title;
    private final String quote;
    private final Bitmap thumbImage;

    public Uri getLocalVideoUrl() {
        return localVideoUrl;
    }

    public String getVideoWebpageUrl() {
        return videoWebpageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getQuote() {
        return quote;
    }

    public Bitmap getThumbImage() {
        return thumbImage;
    }




    public static class Builder {

        private Uri localVideoUrl = null;
        private String videoWebpageUrl = null;
        private String title = null;
        private String quote = null;
        private Bitmap thumbImage = null;

        public Builder(Uri localVideoUrl) {
            this.localVideoUrl = localVideoUrl;
        }

        public Builder videoWebpageUrl(String val) {
            videoWebpageUrl = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder quote(String val) {
            quote = val;
            return this;
        }

        public Builder thumbImage(Bitmap val) {
            thumbImage = val;
            return this;
        }

        public RVideoContent build() {
            return new RVideoContent(this);
        }


    }


    private RVideoContent(Builder builder) {
        localVideoUrl = builder.localVideoUrl;
        videoWebpageUrl = builder.videoWebpageUrl;
        title = builder.title;
        quote = builder.quote;
        thumbImage = builder.thumbImage;
    }
}
