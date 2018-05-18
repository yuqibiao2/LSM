package com.test.lsm.ui.fragment;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.test.lsm.MyApplication;
import com.test.lsm.R;
import com.test.lsm.bean.form.RunRecord;
import com.test.lsm.bean.json.SaveRunRecordReturn;
import com.test.lsm.bean.json.UserLoginReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.utils.BaiduMapUtils;
import com.test.lsm.utils.TimeUtils;
import com.test.lsm.utils.map.MyOrientationListener;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyTimeUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：跑步界面
 *
 * @author yu
 * @version 1.0
 * @date 2018/3/23
 */

public class RunFragment extends LsmBaseFragment{

    private static final String TAG = "RunFragment";

    @BindView(R.id.map_run)
    MapView map_run;
    @BindView(R.id.iv_run_start)
    ImageView iv_run_start;
    @BindView(R.id.iv_run_stop)
    ImageView iv_run_stop;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_run_distance)
    TextView tv_run_distance;

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;

    BitmapDescriptor startBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_startpoint);
    BitmapDescriptor finishBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_finishpoint);

    Polyline mPolyline;
    MapStatus.Builder builder;
    List<LatLng> points = new ArrayList<>();//位置点集合
    LatLng last = new LatLng(0, 0);//上一个定位点
    private MyLocationData locData;
    float mCurrentZoom = 18f;//默认地图缩放比例值
    private int mCurrentDirection = 0;
    private boolean isFirstLoc = true;

    //第一次进入该页面（仅定位）
    private boolean isFirstInit = true;

    private boolean isRunning = false;
    private APIMethodManager apiMethodManager;
    private UserLoginReturn.PdBean user;
    private double distance;
    private String startTime;
    private String stopTime;
    private Gson mGson;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        mGson = new Gson();
        apiMethodManager = APIMethodManager.getInstance();
        MyApplication application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_run;
    }


    @Override
    protected void initView() {
        initMap();
        initLocation();
    }

    float mLastX;

    @Override
    protected void initListener() {
        MyOrientationListener myOrientationListener = new MyOrientationListener(getContext());
        myOrientationListener.start();
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                //将获取的x轴方向赋值给全局变量
                mLastX = x;
                //MyLog.e(TAG ,"onOrientationChanged："+ x);
            }
        });
    }


    /**
     * 初始化基本地图
     */
    private void initMap() {
        //---不显示百度logo
        //map_run.removeViewAt(1);
        //---取消缩放按钮
        map_run.showZoomControls(false);
        //---得到 baidu map对象
        mBaiduMap = map_run.getMap();
        // 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18f).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        initZoom();

    }

    /**
     * 添加地图缩放状态变化监听，当手动放大或缩小地图时，拿到缩放后的比例，然后获取到下次定位，
     *  给地图重新设置缩放比例，否则地图会重新回到默认的mCurrentZoom缩放比例
     */
    private void initZoom() {

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                mCurrentZoom = arg0.zoom;
                MyLog.i(TAG , "mCurrentZoom："+mCurrentZoom);
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 初始化位置
     */
    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(getContext());
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(2000);//2s定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);

        //---开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    /**
     * 位置监听
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .direction(mLastX)
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);

            if (isFirstInit){
                mLocationClient.stop();
              /*  if (mLocationClient.isStarted()) {
                    mLocationClient.stop();
                    MyLog.e(TAG , "mLocationClient.stop()=================================");
                }*/
                isFirstInit = false;
                return;
            }

            //获取经纬度
            //LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
          /*  MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(status);//动画的方式到中间*/

            if (isFirstLoc) {//第一次定位
                LatLng mostAccuracyLocation = getMostAccuracyLocation(location);
                if (mostAccuracyLocation == null) {
                    return;
                }
                isFirstLoc = false;
                points.add(mostAccuracyLocation);
                last = mostAccuracyLocation;
                //显示当前定位点，缩放地图
                locateAndZoom(location, mostAccuracyLocation);
                //标记起点图层位置
                MarkerOptions oStart = new MarkerOptions();// 地图标记覆盖物参数配置类
                oStart.position(points.get(0));// 覆盖物位置点，第一个点为起点
                oStart.icon(startBD);// 设置覆盖物图片
                mBaiduMap.addOverlay(oStart); // 在地图上添加此图层
                startTimer();//开始计时
                isRunning = true;
                hiddenLoadingDialog();
                iv_run_start.setVisibility(View.GONE);
                iv_run_stop.setVisibility(View.VISIBLE);
                return;//画轨迹最少得2个点，首地定位到这里就可以返回了
            }
            //从第二个点开始
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            double distance1 = DistanceUtil.getDistance(last, ll);
            if (distance1<0.5||distance1>10) {//舍去位置太远的点(1s钟位置移动10m不现实)
                return;
            }

            points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

            //MyToast.showShort(getContext(), "latitude：" + ll.latitude + "  longitude：" + ll.longitude);

            last = ll;

            //显示当前定位点，缩放地图
            locateAndZoom(location, ll);

            //清除上一次轨迹，避免重叠绘画
            mBaiduMap.clear();

            //起始点图层也会被清除，重新绘画
            MarkerOptions oStart = new MarkerOptions();
            oStart.position(points.get(0));
            oStart.icon(startBD);
            mBaiduMap.addOverlay(oStart);

            //将points集合中的点绘制轨迹线条图层，显示在地图上
            OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(points);
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            distance = BaiduMapUtils.calcDistance(points);
            tv_run_distance.setText(""+BaiduMapUtils.resolveDistance(distance));
        }

    }

    private void locateAndZoom(final BDLocation location, LatLng ll) {
        locData = new MyLocationData.Builder().accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        builder = new MapStatus.Builder();
        builder.target(ll).zoom(mCurrentZoom);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 首次定位很重要，选一个精度相对较高的起始点
     * 注意：如果一直显示gps信号弱，说明过滤的标准过高了，
     * 你可以将location.getRadius()>25中的过滤半径调大，比如>40，
     * 并且将连续5个点之间的距离DistanceUtil.getDistance(last, ll ) > 5也调大一点，比如>10，
     * 这里不是固定死的，你可以根据你的需求调整，如果你的轨迹刚开始效果不是很好，你可以将半径调小，两点之间距离也调小，
     * gps的精度半径一般是10-50米
     */
    private LatLng getMostAccuracyLocation(BDLocation location) {

        MyLog.e(TAG, "location.getRadius()：" + location.getRadius());


      /*  if (location.getRadius() > 50) {//gps位置精度大于40米的点直接弃用
            return null;
        }*/

        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        MyLog.e(TAG, "latitude：" + ll.latitude + "  longitude：" + ll.longitude);

        if (DistanceUtil.getDistance(last, ll) > 10) {
            last = ll;
            points.clear();//有任意连续两点位置大于10，重新取点
            return null;
        }
        points.add(ll);
        last = ll;
        //有5个连续的点之间的距离小于10，认为gps已稳定，以最新的点为起始点
        if (points.size() >= 3) {
            points.clear();
            return ll;
        }
        return null;
    }


    private boolean isStartTimer = false;
    private long second = 0;

    /**
     * 开始计时
     */
    private void startTimer() {
        isStartTimer = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStartTimer) {
                    try {
                        second++;
                        tv_time.post(new Runnable() {
                            @Override
                            public void run() {
                                if (tv_time!=null){
                                    tv_time.setText(TimeUtils.countTimer(second));
                                }
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 结束计时
     */
    private void stopTimer(){
        second = 0;
        isStartTimer = false;
        tv_time.setText(TimeUtils.countTimer(0L));
    }

    /**
     * 开始跑步
     */

    @OnClick(R.id.iv_run_start)
    public void onRunStart() {
        startTime = MyTimeUtils.getCurrentDateTime();
        showLoadingDialog();
        if (mLocationClient != null && !isRunning) {
            if (!mLocationClient.isStarted()) {
                mLocationClient.start();
            }
            mBaiduMap.clear();
        }
    }

    /**
     * 停止跑步
     */
    @OnClick(R.id.iv_run_stop)
    public void onRunStop() {

        stopTime = MyTimeUtils.getCurrentDateTime();
        //TODO 记录本次跑步的数据
        saveRunRecord();

        String toJson = new Gson().toJson(points);
        //MyLog.e(TAG , toJson);

        showLoadingDialog();
        stopTimer();
        if (mLocationClient != null /*&& mLocationClient.isStarted()*/ && isRunning) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
            if (isFirstLoc) {
                points.clear();
                last = new LatLng(0, 0);
                return;
            }
            MarkerOptions oFinish = new MarkerOptions();// 地图标记覆盖物参数配置类
            oFinish.position(points.get(points.size() - 1));
            oFinish.icon(finishBD);// 设置覆盖物图片
            mBaiduMap.addOverlay(oFinish); // 在地图上添加此图层
            //复位
            points.clear();
            tv_run_distance.setText("0 m");
            last = new LatLng(0, 0);
            isFirstLoc = true;
            iv_run_start.setVisibility(View.VISIBLE);
            iv_run_stop.setVisibility(View.GONE);
            isRunning = false;
            hiddenLoadingDialog();
        }
        isFirstLoc = true;
    }


    private void startLocation(){

    }

    private void stopLocation(){

    }

    /**
     * 保存跑步记录
     *
     */
    private void saveRunRecord() {
        if (points.size()<2){
            MyToast.showLong(getContext() , "您移动的距离太小，记录数据失败！");
            return;
        }
        RunRecord runRecord = new RunRecord();
        runRecord.setUserId(user.getUSER_ID());
        runRecord.setStartTime(startTime);
        runRecord.setStopTime(stopTime);
        runRecord.setDistance(distance);
        runRecord.setCoordinateInfo(mGson.toJson(points));
        runRecord.setRunTime(""+TimeUtils.countTimer(second));
        apiMethodManager.saveRunRecord(runRecord, new IRequestCallback<SaveRunRecordReturn>() {
            @Override
            public void onSuccess(SaveRunRecordReturn result) {
                MyLog.d(TAG , "saveRunRecord==成功=="+result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyLog.e(TAG , "saveRunRecord==异常=="+throwable.getMessage());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        map_run.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        map_run.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        map_run.onDestroy();
        super.onDestroy();
    }


}