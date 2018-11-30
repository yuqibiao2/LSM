package com.test.lsm.ui.fragment.care_group;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetHealthInfoDtlReturn;
import com.test.lsm.bean.json.GetMonitorGroupMemDetailReturn;
import com.test.lsm.global.Const;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：关心群组成员运动轨迹
 *
 * @author yu
 * @version 1.0
 * @date 2018/10/29
 */
public class CareGroupMemRunTraceFragment extends LsmBaseFragment {

    private static final String TAG = "CareGroupMemRunTraceFra";

    @BindView(R.id.map_run)
    MapView map_run;


    private BaiduMap mBaiduMap;
    Polyline mPolyline;

    BitmapDescriptor startBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_startpoint);
    BitmapDescriptor finishBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_finishpoint);
    private APIMethodManager apiMethodManager;
    private Gson mGson;

    @Override
    public int getLayoutId() {

        return R.layout.fragment_care_group_mem_run_trace;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
        apiMethodManager = APIMethodManager.getInstance();
        mGson = new Gson();
    }

    @Override
    protected void initView() {
        initMap();
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
        // 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().zoom(18f).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        // 开启定位图层
        //mBaiduMap.setMyLocationEnabled(true);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void inflateTraceInfo(GetMonitorGroupMemDetailReturn.DataBean.TraceInfoBean traceInfo){

        String coordinateInfo = traceInfo.getCoordinateInfo();
        if (TextUtils.isEmpty(coordinateInfo)) return;
        Type type = new TypeToken<List<LatLng>>() {
        }.getType();
        List<LatLng> latLngs = mGson.fromJson(coordinateInfo, type);
        if (latLngs.size()>2){
            MarkerOptions oStart = new MarkerOptions();//地图标记覆盖物参数配置类
            oStart.position(latLngs.get(0));//覆盖物位置点，第一个点为起点
            oStart.icon(startBD);//设置覆盖物图片
            oStart.zIndex(1);//设置覆盖物Index
            mBaiduMap.addOverlay(oStart); //在地图上添加此图
            MarkerOptions oFinish = new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).icon(finishBD).zIndex(2);
            mBaiduMap.addOverlay(oFinish);
            int color = getContext().getResources().getColor(R.color.colorAccent);
            OverlayOptions ooPolyline = new PolylineOptions().width(8).color(color).points(latLngs);
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            mPolyline.setZIndex(3);
        }
        double lanSum = 0;
        double lonSum = 0;
        for (LatLng latLng : latLngs) {
            lanSum += latLng.latitude;
            lonSum += latLng.longitude;
        }
        LatLng target = new LatLng(lanSum / latLngs.size(), lonSum / latLngs.size());
        locateAndZoom(target);

    }

    private void locateAndZoom(LatLng ll) {
        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(0)
                .direction(0)
                .latitude(ll.latitude)
                .longitude(ll.longitude)
                .build();
        mBaiduMap.setMyLocationData(locationData);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    @Override
    public void onDestroy() {
        map_run.onDestroy();
        super.onDestroy();
    }

}
