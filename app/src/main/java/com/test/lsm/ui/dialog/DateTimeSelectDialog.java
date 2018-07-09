package com.test.lsm.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.test.lsm.R;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;


/**
 * 功能：日期选择
 *
 * @author yu
 * @version 1.0
 * @date 2018/7/5
 */
public class DateTimeSelectDialog extends LsmBaseDialog {


    @BindView(R.id.mcv_hr)
    MaterialCalendarView mcvHr;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.sb_waring_hr)
    SeekBar sbWaringHr;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private int hour = 10;

    public DateTimeSelectDialog(Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams getLayoutParams() {
        return lp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_date_time_select;
    }

    @Override
    protected void initView() {
        mcvHr.addDecorators(new MySelectorDecorator(mContext));
    }

    @Override
    protected void initListener() {

        sbWaringHr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvHour.setText("" + i + "时");
                hour = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDateTimeSelectListener != null) {
                    CalendarDay date = mcvHr.getSelectedDate();
                    if (date==null){
                        MyToast.showLong(mContext , "请选择日期");
                        return;
                    }
                    mOnDateTimeSelectListener.onSelect(date.getYear(), date.getMonth()+1, date.getDay(), hour);
                    dismiss();
                }
            }
        });

    }

    private OnDateTimeSelectListener mOnDateTimeSelectListener;

    public void setmOnDateTimeSelectListener(OnDateTimeSelectListener onDateTimeSelectListener) {
        this.mOnDateTimeSelectListener = onDateTimeSelectListener;
    }

    public interface OnDateTimeSelectListener {
        void onSelect(int year, int mouth, int day, int hour);
    }


    class MySelectorDecorator implements DayViewDecorator {

        private final Drawable drawable;

        public MySelectorDecorator(Context context) {
            drawable = context.getResources().getDrawable(R.drawable.datetime_select);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

}
