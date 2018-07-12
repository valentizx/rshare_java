package me.rex.sdk.twitter;


public interface RTwitterAuthCallback {
    public abstract void onComplete();
    public abstract void onFail(String errorInfo);

}
