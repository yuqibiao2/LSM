package com.test.lsm.ui.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
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
import com.test.lsm.global.Const;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.net.IRequestCallback;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RunRecordDetailFragment extends LsmBaseFragment {

    private static final String TAG = "RunRecordDetailActivity";

    @BindView(R.id.map_run)
    MapView map_run;


    private BaiduMap mBaiduMap;
    private APIMethodManager apiMethodManager;
    MapStatus.Builder builder;

    @Override
    public int getLayoutId() {

        return R.layout.fragment_run_record_detail;
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
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {

                int zIndex = marker.getZIndex();
                MyToast.showLong(getContext(), "zIndex：" + zIndex);

                return true;
            }
        });


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
        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(30.581781, 114.364397));
        latLngs.add(new LatLng(30.580607, 114.365745));
        latLngs.add(new LatLng(30.581291, 114.367281));
        latLngs.add(new LatLng(30.58105, 114.36711));
        latLngs.add(new LatLng(30.580941, 114.366419));
        List<OverlayOptions> options = new ArrayList<>();
        for (LatLng point : latLngs) {
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.ic_me_history_finishpoint);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            options.add(option);
        }
        mBaiduMap.addOverlays(options);

        builder = new MapStatus.Builder();
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
        builder = new MapStatus.Builder();
        builder.target(ll).zoom(18);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    @Override
    public void onDestroy() {
        map_run.onDestroy();
        super.onDestroy();
    }
}
