package com.example.a52374.myapplication.avtivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.a52374.myapplication.NimApplication;
import com.example.a52374.myapplication.R;
import com.example.a52374.myapplication.datamanagement.DemoCache;
import com.example.a52374.myapplication.datamanagement.Preferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by 52374 on 2016/11/13.
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
       // ActionBar bar=getActionBar();
       // bar.hide();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if(DemoCache.getAccount()==null){

                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();} else {

                        doLogin();
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // 登录的方法
    public void doLogin() {
      //final LoginInfo info=   new NimApplication().loginInfo();
        //LoginInfo info = new LoginInfo(account,password); // config...
        final String account=Preferences.getUserAccount();
        final String token= Preferences.getUserToken();
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        Log.i("tmd","登陆成功!!!!! ");
                        //DemoCache.setContext(LoginActivity.this);
                        com.example.a52374.myapplication.datamanagement.Preferences.saveUserAccount(account );
                        com.example.a52374.myapplication.datamanagement.Preferences.saveUserToken(token);

                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.i("tmd","登陆失败 !!!!"+i);
                        //Toast.makeText(LoginActivity.this,"sss",0).show();
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Log.i("tmd","登陆异常！！！！！");
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(new LoginInfo(account,token))
                .setCallback(callback);
    }
}
