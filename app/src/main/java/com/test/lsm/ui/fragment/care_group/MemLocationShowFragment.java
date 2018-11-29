package com.test.lsm.ui.fragment.care_group;

import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.net.APIMethodManager;
import com.test.lsm.ui.activity.CareGroupDetailActivity;
import com.test.lsm.ui.activity.CareGroupMemDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MemLocationShowFragment extends LsmBaseFragment {

    private static final String TAG = "RunRecordDetailActivity";

    private List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> mMemInfoList;

    @BindView(R.id.map_run)
    MapView map_run;


    private BaiduMap mBaiduMap;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_run_record_detail;
    }

    @Override
    public void beforeInit() {
        super.beforeInit();
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
                int index = marker.getZIndex();
                GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo = mMemInfoList.get(index);
                CareGroupMemDetailActivity.startAction(getActivity() , mGson.toJson(memInfo));
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

    public void inflateMemInfo(List<GetMonitorGroupDetailReturn.DataBean.MemInfoListBean> memInfoList){

        mMemInfoList = memInfoList;

        List<OverlayOptions> options = new ArrayList<>();
        double latSum = 0;
        double lonSum = 0;

        for (int i = 0; i < memInfoList.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo = memInfoList.get(i);
            String lat = memInfo.getCurrentLat();
            String lon = memInfo.getCurrentLon();
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)){
                Double latD = Double.valueOf(lat);
                Double lonD = Double.valueOf(lon);
                lonSum += lonD;
                latSum += latD;
                LatLng latLng = new LatLng( latD , lonD);
                String watchingTag = memInfo.getWatchingTag();
                OverlayOptions option = getOption(latLng, watchingTag , i);
                options.add(option);
            }
        }
        //添加标注
        mBaiduMap.addOverlays(options);
        //移动到中心点
        LatLng target = new LatLng(latSum / options.size(), lonSum / options.size());
        locateAndZoom(target);
    }

    private OverlayOptions getOption(LatLng point , String watchingTag , Integer index){

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_blue);
        switch (watchingTag){
            case "1"://blue
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_blue);
                break;
            case "2"://green
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_green);
                break;
            case "3"://yellow
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_yellow);
                break;
            case "4"://purple
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_purple);
                break;
            case "5"://red
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mon_mark_red);
                break;
        }
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .zIndex(index);

        return option;
    }

    @Override
    protected void initData() {
        super.initData();
       /* List<LatLng> latLngs = new ArrayList<>();
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

        double lanSum = 0;
        double lonSum = 0;
        for (LatLng latLng : latLngs) {
            lanSum += latLng.latitude;
            lonSum += latLng.longitude;
        }
        LatLng target = new LatLng(lanSum / latLngs.size(), lonSum / latLngs.size());
        locateAndZoom(target);*/
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
