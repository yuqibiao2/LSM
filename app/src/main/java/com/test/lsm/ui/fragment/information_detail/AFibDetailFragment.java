package com.test.lsm.ui.fragment.information_detail;

import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singularwings.rrislib.RRIsVerifier;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.LsmBleData;
import com.test.lsm.bean.event.SCAutoScanChgEvent;
import com.test.lsm.bean.json.GetAFibExpRecordReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.db.bean.AFibExpRecord;
import com.test.lsm.db.service.AFibExpRecordService;
import com.test.lsm.db.service.inter.IAFibExpRecordService;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.activity.AFibDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.test.lsm.utils.TimeUtils;
import com.yyyu.baselibrary.ui.widget.AdapterLinearLayout;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

import static com.test.lsm.global.SpConstant.SC_AUTO_SCAN;

/**
 * 功能：AFib詳情頁面
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/24
 */
public class AFibDetailFragment extends LsmBaseFragment {

    @BindView(R.id.sc_auto_scan)
    SwitchCompat scAutoScan;
    @BindView(R.id.all_afib)
    AdapterLinearLayout allAfib;
    @BindView(R.id.ll_afib)
    LinearLayout llAfib;
    private MyApplication application;
    private RRIsVerifier mVerifier;
    private IAFibExpRecordService iAFibExpRecordService;
    private AdapterLinearLayout.LinearAdapter adapter;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_afib_detail;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
        apiMethodManager = APIMethodManager.getInstance();
        mVerifier = new RRIsVerifier();
        iAFibExpRecordService = new AFibExpRecordService();
    }

    @Override
    protected void initView() {

        boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);
        scAutoScan.setChecked(isRecord);
    }


    @Override
    protected void initListener() {

        scAutoScan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MySPUtils.put(getContext() , SC_AUTO_SCAN , b);
                EventBus.getDefault().post(new SCAutoScanChgEvent(b));
            }
        });

        application.setOnGetBleDataValueListener(new MyApplication.OnGetBleDataValueListener() {
            @Override
            public void onGet(LsmBleData lsmBleData) {

                boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);

                double rriValue = lsmBleData.getRriValue();
                boolean isExp = mVerifier.feedOneRRI(Double.valueOf(rriValue).intValue());
                if (isExp && isRecord) {
                    initData();
                }
            }
        });

        llAfib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AFibDetailActivity.startAction(getContext() ,user.getUSER_ID());
            }
        });

        allAfib.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AFibDetailActivity.startAction(getContext() , user.getUSER_ID());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        apiMethodManager.getAfibExpRecords(provider, user.getUSER_ID(), 1, 3, new IRequestCallback<GetAFibExpRecordReturn>() {
            @Override
            public void onSuccess(GetAFibExpRecordReturn result) {
                int code = result.getCode();
                if (code==200){
                    GetAFibExpRecordReturn.DataBean data = result.getData();
                    final List<GetAFibExpRecordReturn.DataBean.ListBean> records = data.getList();
                    adapter = new AdapterLinearLayout.LinearAdapter() {
                        @Override
                        public int getItemCount() {
                            return records.size();
                        }

                        @Override
                        public View getView(ViewGroup parent, int position) {
                            View item = LayoutInflater.from(getContext()).inflate(R.layout.all_afib_item, parent, false);
                            GetAFibExpRecordReturn.DataBean.ListBean aFibExpRecord = records.get(position);
                            Long createTime = MyTimeUtils.parseDate(aFibExpRecord.getCreateTime()).getTime();
                            String date = MyTimeUtils.formatDateTime("yyyy/MM/dd", new Date(createTime));
                            String time = MyTimeUtils.formatDateTime("HH:mm:ss", new Date(createTime));
                            TextView tvDate = item.findViewById(R.id.tv_date);
                            TextView tvTime = item.findViewById(R.id.tv_time);
                            tvDate.setText("" + date);
                            tvTime.setText("" + time);
                            return item;
                        }
                    };
                    allAfib.setAdapter(adapter);
                }else{
                    MyToast.showLong(getContext(),"获取AFib信息异常："+result.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(getContext(),"获取AFib信息异常："+throwable.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isRecord = (boolean) MySPUtils.get(getContext() , SC_AUTO_SCAN , false);
        scAutoScan.setChecked(isRecord);
    }
}
