package me.rex.sdk.share;

public class RShare {

    public enum ShareContentType {
        Webpage,
        Photo,
        Video,
        Text,
        Media,
        File,
        Music,
        App
    }

    /**
     * 分享模式仅对部分平台生效, 部分平台直接跳转应用分享并不提供其他形式的分享, 一些平台的分享接口中参数不含 Mode.
     * */
    public enum Mode {
        /**
         * 默认的分享方式
         * Facebook: 优先客户端分享, 客户端无法分享会转由网页形式分享.
         * Twitter: 优先应用内分享.
         * Instagram: 默认客户端分享.
         * */
        Automatic,
        /**
         * 原生应用分享.
         * Facebook、Twitter: 无回调.
         *
         * */
        Native,
        /**
         * 网页分享, 有回调, 仅对Facebook生效.
         * */
        Web,
        /**
         * 反馈网页形式分享, 有回调, 仅对Facebook生效.
         * */
        Feed,
        /**
         * 调用 Android 系统分享.
         * */
        System,
    }


}
