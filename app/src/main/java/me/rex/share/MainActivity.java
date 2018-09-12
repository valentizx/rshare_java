package me.rex.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;


import me.rex.sdk.share.RImageContent;
import me.rex.sdk.share.RShare;
import me.rex.sdk.share.RShareListener;
import me.rex.sdk.share.RShareManager;
import me.rex.sdk.share.RSharePlatform;
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

public class MainActivity extends AppCompatActivity {

    final int PICK_VIDEO_REQUEST_CODE = 10069;
    final int PICK_FILE_REQUEST_CODE = 10070;

    String flag = "";

    final String TAG = "MainActivity========>";
    Bitmap mPhoto;
    Bitmap mPhoto2;


    final String mWebapgeUrl = "https://www.nytimes.com/2018/05/04/arts/music/playlist-christina-aguilera-kanye-west-travis-scott-dirty-projectors.html";
    final String mDescription = "流行天后 Christina Aguilera 时隔六年回归全新录音室专辑「Liberation」于 2018 年 6 月 15 日发布.";
    final String mHashTag = "#liberation";
    final String mTitle = "Liberation";
    ArrayList<String> mImagePathList;
    ArrayList<Uri> mImageUris;

    final String mAppUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.uttermostapps.christinaaguilera&from=singlemessage";
    final String mMusicWebapgeUrl = "http://url.cn/5tZF9KT";

    final String mAudioStreamUrl = "http://10.136.9.109/fcgi-bin/fcg_music_get_playurl.fcg?song_id=1234&redirect=0&filetype=mp3&qqmusic_fromtag=15&app_id=100311325&app_key=b233c8c2c8a0fbee4f83781b4a04c595&device_id=1234";
    final String mVideoUrl = "https://www.youtube.com/watch?v=DSRSgMp5X1w";

    final String mThumbImage = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1509086430,2757737602&fm=11&gp=0.jpg";

    final RShareListener mListener = new RShareListener() {
        @Override
        public void onComplete(RSharePlatform.Platform platform) {
            Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFail(RSharePlatform.Platform platform, String errorInfo) {
            Toast.makeText(MainActivity.this, "分享失败" + errorInfo, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(RSharePlatform.Platform platform) {
            Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhoto = BitmapFactory.decodeResource(this.getApplicationContext()
                .getResources(), R.drawable.c);
        mPhoto2 = BitmapFactory.decodeResource(this.getApplicationContext()
                .getResources(), R.drawable.c_1);



        Log.v(TAG, getFilesDir().getAbsolutePath());

        mImagePathList = new ArrayList<String>();

        mImagePathList.add(mThumbImage);
        //mImagePathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/cover1.jpg")));
        mImagePathList.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1657378823,3340694539&fm=27&gp=0.jpg");
        mImagePathList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3289990191,2537468963&fm=27&gp=0.jpg");
        mImagePathList.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2034202254,690170313&fm=27&gp=0.jpg");



        mImageUris = new ArrayList<Uri>();
        mImageUris.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/namie.jpeg")));
//        mImageUris.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/cover8.jpg")));
//        mImageUris.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/cover8.jpg")));
//        mImageUris.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/cover8.jpg")));




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data == null) return;
        Uri uri = data.getData();
        if (requestCode == PICK_VIDEO_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {



                if (flag.equals("QQZONE")) {
                    RQqManager.getInstance().publishVideoToZone(this, uri,
                            mDescription,
                            mListener);
                }
                if (flag.equals("FACEBOOK")) {
                    RFacebookManager.getInstance().shareLocalVideo(this, uri);
                }

                if (flag.equals("INSTAGRAM")) {
                    RInstagramManager.getInstance().shareVideo(this, uri);
                }
                if (flag.equals("SINA")) {
                    RSinaWeiboManager.getInstance().shareLocalVideo(this, uri, mDescription,
                            true , mListener);
                }



            }
        }
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            if (flag.equals("WX")) {
                RWechatManager.getInstance().shareFile(this,uri, mTitle, mPhoto,RWechatManager
                        .TargetScene
                        .Session, mListener );
            }
        }
    }


    //分享
    public void share(View view) {

        Button btn = (Button)view;
        switch (btn.getId()) {
            case R.id.fb_wb_btn:
                RFacebookManager.getInstance().shareWebpage(this, mWebapgeUrl, mDescription,
                        mHashTag, RShare.Mode.Feed
                        ,mListener);
                break;
            case R.id.fb_ph_btn:

                ArrayList<Bitmap> photos = new ArrayList<Bitmap>();
                photos.add(mPhoto);
                photos.add(mPhoto);
                photos.add(mPhoto);
                photos.add(mPhoto);
                photos.add(mPhoto);
                photos.add(mPhoto);
                //photos.add(mPhoto);

                RFacebookManager.getInstance().sharePhoto(this, photos);
                break;
            case  R.id.fb_vd_btn:
                flag = "FACEBOOK";
                chooseVideo();
                break;
            case R.id.tw_app_btn:
                RTwitterManager.getInstance().share(this, mWebapgeUrl, mDescription, mPhoto,
                        mHashTag, RShare.Mode.Automatic, mListener);

                break;
            case R.id.tw_inner_btn:
                RTwitterManager.getInstance().share(this, mWebapgeUrl, mDescription, mPhoto,
                        null, RShare.Mode.Native, mListener);
                break;
            case R.id.ins_app_btn:
                RInstagramManager.getInstance().shareImage(this, mPhoto);
                break;
            case R.id.ins_sys_btn:
                RInstagramManager.getInstance().shareImage(this, mPhoto);
                break;

            case R.id.ins_vid_btn:
                flag = "INSTAGRAM";
                Intent intent3 = new Intent();
                intent3.setType("video/*");
                intent3.setAction(Intent.ACTION_GET_CONTENT);
                intent3.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent3,
                        PICK_VIDEO_REQUEST_CODE);
                break;

            case R.id.wx_text_btn:
                RWechatManager.getInstance().shareText(this, mDescription, RWechatManager
                        .TargetScene.Session, mListener);
                break;
            case R.id.wx_img_btn:
                RWechatManager.getInstance().shareImage(this, mPhoto2, RWechatManager.TargetScene
                        .Session, mListener);
                break;
            case R.id.wx_web_btn:
                RWechatManager.getInstance().shareWebpage(this, mWebapgeUrl, mTitle,
                        mDescription, mPhoto2,RWechatManager.TargetScene.Session, mListener);
                break;
            case R.id.wx_music_btn:
                RWechatManager.getInstance().shareMusic(this, mAudioStreamUrl, mTitle,
                        mDescription, mPhoto, mMusicWebapgeUrl,RWechatManager.TargetScene.Session, mListener);
                break;
            case R.id.wx_video_btn:
                RWechatManager.getInstance().shareVideo(this, mVideoUrl, mTitle, mDescription,
                        mPhoto, RWechatManager.TargetScene.Session, mListener);
                break;
            case R.id.wx_mini_btn:
                RWechatManager.getInstance().shareMiniProgram(this, "gh_d43f693ca31f",
                        "pages/play/index?cid=fvue88y1fsnk4w2&ptag=vicyao&seek=3219",
                        RWechatManager.MiniProgramType.RELEASE, mWebapgeUrl, mTitle,
                        mDescription, mPhoto, RWechatManager.TargetScene.Session, mListener);
                break;
            case R.id.wx_file_btn:
                flag = "WX";
                chooseFile();
                break;
            case R.id.wb_text_btn:
                RSinaWeiboManager.getInstance().shareText(this, mDescription, mListener);
                break;
            case R.id.wb_img_btn:
                ArrayList<Bitmap> photos2 = new ArrayList<Bitmap>();
                photos2.add(mPhoto);

                RSinaWeiboManager.getInstance().sharePhoto(this, photos2, mDescription, false,
                        mListener
                );

                break;
            case R.id.wb_web_btn:
                RSinaWeiboManager.getInstance().shareWebpage(this, mWebapgeUrl, mTitle,
                        mDescription,
                        mPhoto2,
                        mListener);
                break;
            case R.id.wb_vd_btn:
                flag = "SINA";
                chooseVideo();
                break;

            case R.id.qq_default_btn:
                RQqManager.getInstance().shareWebpage(this, mWebapgeUrl, mTitle, mDescription,
                        mThumbImage, mListener);
                break;
            case R.id.qq_img_btn:
                RQqManager.getInstance().shareImage(this, mPhoto, mListener);
                break;
            case R.id.qq_music_btn:
                RQqManager.getInstance().shareMusic(this, mAudioStreamUrl, mMusicWebapgeUrl, mTitle,
                        mDescription, mThumbImage, mListener);
                break;
            case R.id.qq_app_btn:
                RQqManager.getInstance().shareApp(this, mAppUrl, mTitle, mDescription,
                        mThumbImage, mListener);
                break;
            case R.id.qz_web_btn:
                RQqManager.getInstance().shareWebpageToZone(this, mWebapgeUrl, mTitle,
                        mDescription, mImagePathList, mListener);
                break;
            case R.id.qz_imgs_btn:
                ArrayList<Bitmap> localePhotos = new ArrayList<Bitmap>();
                localePhotos.add(mPhoto);

                RQqManager.getInstance().publishImagesToZone(this, localePhotos, mDescription, mListener);
                break;
            case R.id.qz_video_btn:
                flag = "QQZONE";
                chooseVideo();
                break;
            case R.id.wsa_img_btn:
                RWhatsAppManager.getInstance().share(this, mPhoto, mDescription);
                break;
            case R.id.gp_web_btn:
                RGooglePlusManager.getInstance().share(this, mWebapgeUrl, mDescription);
                break;
            case R.id.tmb_img_btn:
                RTumblrManager.getInstance().shareImage(this, mThumbImage, mDescription,
                        mWebapgeUrl, mListener);
                break;
            case R.id.tmb_text_btn:
                RTumblrManager.getInstance().shareText(this, mDescription, mTitle,
                        mWebapgeUrl, mListener);
                break;
            case R.id.l_img_btn:
                RLineManager.getInstance().shareImage(this, mPhoto);
                break;
            case R.id.l_text_btn:
                RLineManager.getInstance().shareText(this, mDescription);
                break;
            case R.id.pin_img_btn:
                RPinterestManager.getInstance().shareImage(this, mThumbImage);
                break;

            default:break;
        }
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,
                PICK_VIDEO_REQUEST_CODE);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,
                PICK_FILE_REQUEST_CODE);
    }


}
