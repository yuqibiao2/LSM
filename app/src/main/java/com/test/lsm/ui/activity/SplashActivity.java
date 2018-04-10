package com.test.lsm.ui.activity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.test.lsm.R;

import butterknife.BindView;

/**
 * 功能：启动页
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class SplashActivity extends LsmBaseActivity{

    @BindView(R.id.iv_splash)
    ImageView iv_splash;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void afterInit() {
        super.afterInit();
        Animation animation = AnimationUtils.loadAnimation(this , R.anim.splash_anime);
        iv_splash.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(false){//登录了
                    MainActivity.startAction(SplashActivity.this);
                }else{
                    LoginActivity.startAction(SplashActivity.this);
                }
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
