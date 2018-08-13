package me.rex.sdk.share;


public final class RTextContent {

    private final String body;
    private final String title;
    private final String webpageUrl;

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public static class Builder {

        private String body = null;
        private String title = null;
        private String webpageUrl = null;

        public Builder(String body) {
            this.body = body;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }
        public Builder webpageUrl(String val) {
            webpageUrl = val;
            return this;
        }

        public RTextContent build() {
            return new RTextContent(this);
        }
    }

    private RTextContent(Builder builder) {
        body = builder.body;
        title = builder.title;
        webpageUrl = builder.webpageUrl;
    }
}
