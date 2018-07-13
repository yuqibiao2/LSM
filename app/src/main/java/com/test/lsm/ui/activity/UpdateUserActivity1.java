package com.test.lsm.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.form.UserUpdateVo;
import com.test.lsm.bean.json.UserLoginReturn;
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
public class UpdateUserActivity1 extends LsmBaseActivity {

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
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;

    private Gson mGson;

    private int year, month, day;
    private Calendar cal;
    private DatePickerDialog datePickerDialog;
    private UserLoginReturn.PdBean user;
    private UserUpdateVo userUpdateVo;

    @Override
    public int getLayoutId() {

        return R.layout.activity_update_user1;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        Intent intent = getIntent();
        mGson = new Gson();
        getDate();
        userUpdateVo = new UserUpdateVo();
        MyApplication application = (MyApplication) getApplication();
        user = application.getUser();
        userUpdateVo.setUSER_ID(intent.getIntExtra("userId" , -1));
        userUpdateVo.setHEALTH_PARAM(""+MySPUtils.get(this , "waringHr" , -1));
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                etUserBirthday.setText(year + "-" + (++month) + "-" + day);
            }
        };
        datePickerDialog = new DatePickerDialog(UpdateUserActivity1.this, 0, listener, year, month, day);
        String userSex = user.getUSER_SEX();
            switch (Integer.parseInt(userSex)) {
            case 0:
                rbMale.setChecked(true);
                userUpdateVo.setUSER_SEX(0 + "");
                break;
            case 1:
                rbFemale.setChecked(true);
                userUpdateVo.setUSER_SEX(1 + "");
                break;
        }
        String birthday = user.getBIRTHDAY();
        etUserBirthday.setText("" + birthday);
        //String[] split = birthday.split("-");
        //datePickerDialog.updateDate(Integer.parseInt(split[0]), Integer.parseInt(split[1]) + 1, Integer.parseInt(split[2]));
        etUserHeight.setText(user.getUSER_HEIGHT());
        etUserWeight.setText(user.getUSER_WEIGHT());
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
                MyLog.e("onCheckedChanged================="+i);
                switch (i) {
                    case R.id.rb_male://男
                        userUpdateVo.setUSER_SEX(0 + "");
                        break;
                    case R.id.rb_female://女
                        userUpdateVo.setUSER_SEX(1 + "");
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

        userUpdateVo.setBIRTHDAY(birthday);
        userUpdateVo.setUSER_HEIGHT(height);
        userUpdateVo.setUSER_WEIGHT(weight);

        UpdateUserActivity2.startAction(this, mGson.toJson(userUpdateVo));

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEventBus(String message) {
        if ("register_finished".equals(message)) {
            finish();
        }
    }


    public static void startAction(Activity activity,Integer userId) {
        Intent intent = new Intent(activity, UpdateUserActivity1.class);
        intent.putExtra("userId" , userId);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
