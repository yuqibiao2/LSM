package com.test.lsm.ui.fragment;

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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.test.lsm.R;
import com.test.lsm.utils.BaiduMapUtils;
import com.test.lsm.utils.TimeUtils;
import com.yyyu.baselibrary.utils.MyLog;
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

public class RunFragment extends LsmBaseFragment implements SensorEventListener {

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
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private MyLocationData locData;
    float mCurrentZoom = 17f;//默认地图缩放比例值
    private int mCurrentDirection = 0;
    private boolean isFirstLoc = true;

    private boolean isRunning = false;

    @Override
    protected void beforeInit() {
        super.beforeInit();
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

    @Override
    protected void initListener() {

    }


    /**
     * 初始化基本地图
     */
    private void initMap() {
        //---不显示百度logo
        map_run.removeViewAt(1);
        //---取消缩放按钮
        map_run.showZoomControls(false);
        //---得到 baidu map对象
        mBaiduMap = map_run.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        /**
         * 添加地图缩放状态变化监听，当手动放大或缩小地图时，拿到缩放后的比例，然后获取到下次定位，
         *  给地图重新设置缩放比例，否则地图会重新回到默认的mCurrentZoom缩放比例
         */
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                mCurrentZoom = arg0.zoom;
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
        option.setScanSpan(1000);//10s定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }


    double lastX;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];

        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;

            if (isFirstLoc) {
                lastX = x;
                return;
            }

            locData = new MyLocationData.Builder().accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat).longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
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
            MyLog.e(TAG, "latitude：" + ll.latitude + "  longitude：" + ll.longitude);
            MyToast.showShort(getContext(), "latitude：" + ll.latitude + "  longitude：" + ll.longitude);
            if (DistanceUtil.getDistance(last, ll) < 2 || DistanceUtil.getDistance(last, ll)>100) {//舍去位置太近和太远的点
                return;
            }

            points.add(ll);//如果要运动完成后画整个轨迹，位置点都在这个集合中

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
            double distance = BaiduMapUtils.calcDistance(points);
            tv_run_distance.setText(""+distance);

        }

    }

    private void locateAndZoom(final BDLocation location, LatLng ll) {
        mCurrentLat = location.getLatitude();
        mCurrentLon = location.getLongitude();
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
        showLoadingDialog();
        stopTimer();
        if (mLocationClient != null && mLocationClient.isStarted() && isRunning) {
            mLocationClient.stop();
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
            last = new LatLng(0, 0);
            isFirstLoc = true;
            iv_run_start.setVisibility(View.VISIBLE);
            iv_run_stop.setVisibility(View.GONE);
            isRunning = false;
            hiddenLoadingDialog();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        //---开启定位
        mBaiduMap.setMyLocationEnabled(true);
       /* if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }*/
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
        mBaiduMap.setMyLocationEnabled(false);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        map_run.onDestroy();
        super.onDestroy();
    }


}
