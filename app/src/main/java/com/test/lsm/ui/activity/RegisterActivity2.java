package com.test.lsm.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.clj.fastble.utils.HexUtil;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.UserBean;
import com.test.lsm.bean.form.UserRegVo;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.Calendar;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 功能：注册界面2
 *
 * @author yu
 * @version 1.0
 * @date 2018/4/8
 */
public class RegisterActivity2 extends LsmBaseActivity {

    private static final String TAG = "RegisterActivity2";

    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
 /*   @BindView(R.id.et_username)
    EditText etUsername;*/
    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.et_user_birthday)
    EditText etUserBirthday;
    @BindView(R.id.til_birthday)
    TextInputLayout tilBirthday;
    @BindView(R.id.et_user_height)
    EditText etUserHeight;
    @BindView(R.id.til_height)
    TextInputLayout tilHeight;
    @BindView(R.id.et_user_weight)
    EditText etUserWeight;
    @BindView(R.id.til_weight)
    TextInputLayout tilWeight;

    private String tel;
    private String pwd;
    private Gson mGson;

    private int year, month, day;
    private Calendar cal;
    private DatePickerDialog datePickerDialog;
    private UserRegVo userRegVo;

    @Override
    public int getLayoutId() {

        return R.layout.activity_register2;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        Intent intent = getIntent();
        mGson = new Gson();
        tel = intent.getStringExtra("tel");
        pwd = intent.getStringExtra("pwd");
        getDate();
        userRegVo = new UserRegVo();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                etUserBirthday.setText(year + "-" + (++month) + "-" + day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
            }
        };
        //后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        datePickerDialog = new DatePickerDialog(RegisterActivity2.this, 0, listener, year, month, day);
    }

    //获取当前日期
    private void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void initListener() {
        etUserBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case 1://男
                        userRegVo.setUSER_SEX(0+"");
                        break;
                    case 2://女
                        userRegVo.setUSER_SEX(1+"");
                        break;
                }
            }
        });

    }

    public void toNext(View view) {
     /*   String username = etUsername.getText().toString();*/
        String birthday = etUserBirthday.getText().toString();
        String height = etUserHeight.getText().toString();
        String weight = etUserWeight.getText().toString();

    if (TextUtils.isEmpty(birthday)) {
            MyToast.showShort(this, "生日不能为空");
            return;
        } else if (TextUtils.isEmpty(birthday)) {
            MyToast.showShort(this, "身高不能为空");
            return;
        } else if (TextUtils.isEmpty(birthday)) {
            MyToast.showShort(this, "体重不能为空");
            return;
        }

      /*  userRegVo.setUSERNAME(username);*/
        userRegVo.setPASSWORD(pwd);
        userRegVo.setPHONE(tel);
        userRegVo.setUSER_SEX(0 + "");//TODO
        userRegVo.setBIRTHDAY(birthday);
        userRegVo.setUSER_HEIGHT(height);
        userRegVo.setUSER_WEIGHT(weight);

        RegisterActivity3.startAction(this, mGson.toJson(userRegVo));

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEventBus(String message) {
        if ("register_finished".equals(message)) {
            finish();
        }
    }


    public static void startAction(Activity activity, String tel, String pwd) {
        Intent intent = new Intent(activity, RegisterActivity2.class);
        intent.putExtra("tel", tel);
        intent.putExtra("pwd", pwd);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
