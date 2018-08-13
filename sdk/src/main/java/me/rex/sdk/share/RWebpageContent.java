package me.rex.sdk.share;

import android.graphics.Bitmap;

final public class RWebpageContent {

    private final String webpageUrl;
    private final String title;
    private final String quote;
    private final String hashTag;
    private final Bitmap thumbImage;
    private final String thumbImageUrl;

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getQuote() {
        return quote;
    }

    public String getHashTag() {
        return hashTag;
    }

    public Bitmap getThumbImage() {
        return thumbImage;
    }

    public String getThumbImageUrl() {
        return thumbImageUrl;
    }

    public static class Builder {
        private String webpageUrl = null;
        private String title = null;
        private String quote = null;
        private String hashTag = null;
        private Bitmap thumbImage = null;
        private String thumbImageUrl = null;

        public Builder(String webpageUrl) {
            this.webpageUrl = webpageUrl;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder quote(String val) {
            quote = val;
            return this;
        }
        public Builder hashTag(String val) {
            hashTag = val;
            return this;
        }
        public Builder thumbImage(Bitmap val) {
            thumbImage = val;
            return this;
        }
        public Builder thumbImageUrl(String val) {
            thumbImageUrl = val;
            return this;
        }

        public RWebpageContent build() {
            return new RWebpageContent(this);
        }
    }

    private RWebpageContent(Builder builder) {
        webpageUrl = builder.webpageUrl;
        title = builder.title;
        quote = builder.quote;
        hashTag = builder.hashTag;
        thumbImage = builder.thumbImage;
        thumbImageUrl = builder.thumbImageUrl;
    }
}
