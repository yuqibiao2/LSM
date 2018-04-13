package com.test.lsm.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.UserBean;
import com.test.lsm.utils.LoginRegUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.lsmalgorithm.MyLib;

import butterknife.BindView;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class LoginActivity extends LsmBaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_user_tel)
    EditText etUserTel;
    @BindView(R.id.til_tel)
    TextInputLayout tilTel;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
    }

    @Override
    protected void initView() {
        LoginRegUtils.checkEdit(tilTel, LoginRegUtils.CheckType.tel);
        LoginRegUtils.checkEdit(tilPwd, LoginRegUtils.CheckType.pwd);
    }

    @Override
    protected void initListener() {

    }

    public void toLogin(View view) {

       /* double[] doubles1=new double[]{74,18021,17941,17874,17825,17786,17746,17706,17690,17702,17731,17756,17774,17788,17772,17761,17741,17662,17599,17588,17595,17592,17585,17610,17638,17694,17804,17980,18117,18154,18165,18214,18283,18235,18064,17901,17698,17427,17350,17382,17461,17551,17613,17650,17662,17676,17726,17758,17775,17796,17818,17822,17819,17823,17821,17824,17844,17871,17866,17860,17879,17895,17867,17824,17802,17812,17825,17848,17872,17892,17918,17947,17999,18040,18085,18135,18171,18202,18257,18317,18357,18392,18441,18476,18495,18532,18559,18566,18551,18532,18501,18470,18443,18430,18418,18342,18179,17943,17745,17689,17696,17696,17662,17607,17555,17508,17473,17453,17476,17534,17579,17617,17635,17642,17648,17663,17674,17702,17728,17726,17691,17659,17653,17636,17601,17591,17595,17604,17630,17658,17708,17764,17793,17791,17804,17836,17864,17887,17893,17885,17874,17878,17890,17888,17875,17879,17926,17972,17968,17917,17878,17886,17924,17933,17931,17939,17941,17920,17908,17947,18014,18077,18118,18110,18055,18009,18000,18043,18085,18078,18076,18066,18065,18060,18050,18052,18078,18103,18101,18112,18123,18123,18112,18102,18114,18134,18127,18107,18081,18080,18075,18019,17936,17845,17719,17539,17384,17299,17275,17299,17338,17352,17339,17330,17349,17340,17304,17299,17317,17357,17376,17358,17351,17344,17367,17438,17587,17785,17903,17963,18004,18099,18144,18023,17912,17783,17461,17196,17174,17244,17365,17460,17504,17553,17602,17657,17691,17692,17677,17681,17726,17758,17702,17594,17504,17495,17526,17540,17540,17544,17550,17564,17581,17598,17627,17659,17683,17709,17743,17750,17749,17767,17801,17831,17865,17909,17952,18006,18062,18122,18176,18219,18259,18299,18321};
        for (double d : doubles1){
            int i1 = MyLib.countHeartRate(d);
            if (i1>0){
                MyLog.e(TAG , "==countHeartRate==="+i1);
            }
        }*/

        String tel = etUserTel.getText().toString();
        String pwd = etUserPwd.getText().toString();

        showLoadDialog("登录中....");

        String userStr = (String) MySPUtils.get(this, "register_user", "");
        UserBean userBean = new Gson().fromJson(userStr, UserBean.class);
        if (userBean == null) {
            MyToast.showShort(this, "该用户不存在");
        } else {
            if (pwd.equals(userBean.getPassword()) && tel.equals(userBean.getTel())){
                MainActivity.startAction(this);
                finish();
            }else{
                MyToast.showShort(this, "用户名或密码错误");
            }
        }
        hiddenLoadDialog();
    }

    public void toRegister(View view) {
        RegisterActivity1.startAction(this);
    }


    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
