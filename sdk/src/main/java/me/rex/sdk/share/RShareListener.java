package me.rex.sdk.share;

public interface RShareListener {

    public abstract void onComplete(RSharePlatform.Platform platform);
    public abstract void onFail(RSharePlatform.Platform platform, String errorInfo);
    public abstract void onCancel(RSharePlatform.Platform platform);
}
