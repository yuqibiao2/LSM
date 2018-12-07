package com.test.lsm.ui.fragment.care_group;

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
import com.baidu.mapapi.model.LatLngBounds;
import com.google.gson.Gson;
import com.test.lsm.R;
import com.test.lsm.bean.json.GetMonitorGroupDetailReturn;
import com.test.lsm.ui.activity.CareGroupMemDetailActivity;
import com.test.lsm.ui.fragment.LsmBaseFragment;

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
        mBaiduMap.clear();
        mMemInfoList = memInfoList;
        List<OverlayOptions> options = new ArrayList<>();
        List<LatLng> latLngList  = new ArrayList<>();
        for (int i = 0; i < memInfoList.size(); i++) {
            GetMonitorGroupDetailReturn.DataBean.MemInfoListBean memInfo = memInfoList.get(i);
            String lat = ""+memInfo.getCurrentLat();
            String lon = ""+memInfo.getCurrentLon();
            if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)){
                Double latD = Double.valueOf(lat);
                Double lonD = Double.valueOf(lon);
                LatLng latLng = new LatLng( latD , lonD);
                String watchingTag = memInfo.getWatchingTag();
                OverlayOptions option = getOption(latLng, watchingTag , i);
                options.add(option);
                latLngList.add(latLng);
            }
        }
        //添加标注
        mBaiduMap.addOverlays(options);
        //使所有坐标都在屏幕内显示
        fitAndZoom(latLngList);
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
    }

    /**
     * 定位到某一坐标
     *
     * @param ll
     */
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

    /**
     *使所有坐标都在屏幕内显示
     * @param latLngList
     */
    private void fitAndZoom(List<LatLng> latLngList){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng latLng : latLngList){
            builder = builder.include(latLng);
        }
        LatLngBounds latlngBounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(latlngBounds,map_run.getWidth(),map_run.getHeight()/4*3);
        mBaiduMap.animateMapStatus(u);
    }


    @Override
    public void onDestroy() {
        map_run.onDestroy();
        super.onDestroy();
    }
}
