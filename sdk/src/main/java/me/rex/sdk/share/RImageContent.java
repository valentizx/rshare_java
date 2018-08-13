package me.rex.sdk.share;

import android.graphics.Bitmap;

final public class RImageContent {

    private final Bitmap image;
    private final String imageUrl;
    private final String title;
    private final String quote;
    private final String webpageUrl;


    public Bitmap getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getQuote() {
        return quote;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }




    public static class Builder {
        private Bitmap image = null;
        private String imageUrl = null;
        private String title = null;
        private String quote = null;
        private String webpageUrl = null;




        public Builder(Bitmap image, String imageUrl) {
            this.image = image;
            this.imageUrl = imageUrl;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder quote(String val) {
            quote = val;
            return this;
        }

        public Builder webpageUrl(String val) {
            webpageUrl = val;
            return this;
        }


        public RImageContent build() {
            return new RImageContent(this);
        }
    }

    private RImageContent(Builder builder) {
        image = builder.image;
        imageUrl = builder.imageUrl;
        title = builder.title;
        quote = builder.quote;
        webpageUrl = builder.webpageUrl;

    }

}
