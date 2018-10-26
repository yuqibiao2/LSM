package com.yyyu.baselibrary.template;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.yyyu.baselibrary.R;
import com.yyyu.baselibrary.utils.WindowUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 自定义Dialog的base类
 *
 * 查找控件使用 getView
 *
 * @author yyyu
 * @date 2016-5-25
 */
public abstract class BaseDialog extends Dialog implements View.OnClickListener {

    protected Context mContext;
    protected WindowManager.LayoutParams lp;
    protected View rootView;

    /**
     * View的容器
     */
    private Map<Integer, View> viewContainer = new HashMap<>();

    public BaseDialog(Context context) {
        this(context, R.style.dialog);
    }

    private BaseDialog(Context context, int theme) {
        super(context, R.style.dialog);
        this.mContext = context;
        lp = getWindow().getAttributes();
        int width = WindowUtils.getSize(mContext)[0];
        lp.width = width * 4 / 5;
        getWindow().setWindowAnimations(R.style.dialog_anim);
        this.setCanceledOnTouchOutside(true);//默认点击外面可消失
        rootView = LayoutInflater.from(context).inflate(getLayoutId(), null);
        setContentView(rootView);
        getWindow().setAttributes(getLayoutParams());
    }

    /**
     * 设置LayoutParams
     *   lp.width = WindowUtils.getSize(mContext)[1] / 2;
     *   lp.height =  WindowUtils.getSize(mContext)[1]/2;
     * @return
     */
    protected  WindowManager.LayoutParams getLayoutParams(){
        return lp;
    }

    public void beforeInit() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    /**
     * Dialog 界面 id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 设置监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    protected void afterInit(){

    }

    /**
     * 得到资源文件中得String
     *
     * @param resId
     * @return
     */
    protected String getStr(int resId){
        return mContext.getResources().getString(resId);
    }

    /**
     * 得到Dialog界面上的控件
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int viewId) {
        if (viewContainer.containsKey(viewId)) {
            return (T) viewContainer.get(viewId);
        }
        return (T) rootView.findViewById(viewId);
    }

    /**
     * 注册点击事件监听
     */
    protected void addOnClickListener(int... viewids) {
        for (int viewId : viewids) {
            getView(viewId).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewContainer.clear();
    }
}