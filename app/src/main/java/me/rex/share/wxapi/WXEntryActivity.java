package me.rex.share.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import me.rex.sdk.wechat.RWechatManager;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private RWechatManager mManager = RWechatManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!mManager.handleIntent(this, getIntent())) {
            finish();
        }


//        try {
//            if (!mApi.handleIntent(getIntent(),this)) {
//                finish();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (!mManager.handleIntent(this, getIntent())) {
            finish();
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        mManager.onResq(this, baseResp);

    }

    @Override
    public void onReq(BaseReq baseReq) {
        mManager.onReq(this, baseReq);
    }
}
