package com.test.lsm.ui.fragment;

import android.graphics.Color;
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
    Polyline mPolyline;

    BitmapDescriptor startBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_startpoint);
    BitmapDescriptor finishBD = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_me_history_finishpoint);
    private APIMethodManager apiMethodManager;
    private int recordId;
    private Gson mGson;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private InfoWindow mInfoWindow;
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
        //TODO 修改
        recordId = 505;
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
        apiMethodManager.getHealthInfoDtl("" + recordId, new IRequestCallback<GetHealthInfoDtlReturn>() {
            @Override
            public void onSuccess(GetHealthInfoDtlReturn result) {
                String code = result.getResult();
                if ("01".equals(code)) {
                    GetHealthInfoDtlReturn.PdBean pd = result.getPd();
                    String record = pd.getRecord();
                    Type type = new TypeToken<List<LatLng>>() {
                    }.getType();
                    List<LatLng> latLngs = mGson.fromJson(record, type);

                    builder = new MapStatus.Builder();
                    double lanSum = 0;
                    double lonSum = 0;
                    for (LatLng latLng : latLngs) {
                        lanSum += latLng.latitude;
                        lonSum += latLng.longitude;
                    }
                    LatLng target = new LatLng(lanSum / latLngs.size(), lonSum / latLngs.size());
                    builder.target(target).zoom(18f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    MarkerOptions oStart = new MarkerOptions();//地图标记覆盖物参数配置类
                    oStart.position(latLngs.get(0));//覆盖物位置点，第一个点为起点
                    oStart.icon(startBD);//设置覆盖物图片
                    oStart.zIndex(1);//设置覆盖物Index
                    mMarkerA = (Marker) (mBaiduMap.addOverlay(oStart)); //在地图上添加此图
                    //添加终点图层
                    MarkerOptions oFinish = new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).icon(finishBD).zIndex(2);
                    mMarkerB = (Marker) (mBaiduMap.addOverlay(oFinish));

                    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                        public boolean onMarkerClick(final Marker marker) {

                            if (marker.getZIndex() == mMarkerA.getZIndex()) {//如果是起始点图层
                                TextView textView = new TextView(getContext());
                                textView.setText("起点");
                                textView.setTextColor(Color.BLACK);
                                textView.setGravity(Gravity.CENTER);
                                textView.setBackgroundResource(R.drawable.popup);

                                //设置信息窗口点击回调
                                InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
                                    public void onInfoWindowClick() {
                                        Toast.makeText(getContext(), "这里是起点", Toast.LENGTH_SHORT).show();
                                        mBaiduMap.hideInfoWindow();//隐藏信息窗口
                                    }
                                };
                                LatLng latLng = marker.getPosition();//信息窗口显示的位置点
                                /**
                                 * 通过传入的 bitmap descriptor 构造一个 InfoWindow
                                 * bd - 展示的bitmap
                                 position - InfoWindow显示的位置点
                                 yOffset - 信息窗口会与图层图标重叠，设置Y轴偏移量可以解决
                                 listener - 点击监听者
                                 */
                                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(textView), latLng, -47, listener);
                                mBaiduMap.showInfoWindow(mInfoWindow);//显示信息窗口

                            } else if (marker.getZIndex() == mMarkerB.getZIndex()) {//如果是终点图层
                                Button button = new Button(getContext());
                                button.setText("终点");
                                button.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Toast.makeText(getContext(), "这里是终点", Toast.LENGTH_SHORT).show();
                                        mBaiduMap.hideInfoWindow();
                                    }
                                });
                                LatLng latLng = marker.getPosition();
                                /**
                                 * 通过传入的 view 构造一个 InfoWindow, 此时只是利用该view生成一个Bitmap绘制在地图中，监听事件由自己实现。
                                 view - 展示的 view
                                 position - 显示的地理位置
                                 yOffset - Y轴偏移量
                                 */
                                mInfoWindow = new InfoWindow(button, latLng, -47);
                                mBaiduMap.showInfoWindow(mInfoWindow);
                            }
                            return true;
                        }
                    });

                    mBaiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
                        @Override
                        public boolean onPolylineClick(Polyline polyline) {
                            if (polyline.getZIndex() == mPolyline.getZIndex()) {
                                Toast.makeText(getContext(), "点数：" + polyline.getPoints().size() + ",width:" + polyline.getWidth(), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                    OverlayOptions ooPolyline = new PolylineOptions().width(13).color(0xAAFF0000).points(latLngs);
                    mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
                    mPolyline.setZIndex(3);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                MyToast.showLong(getContext(), "数据获取失败：" + throwable.getMessage());
            }
        });
    }


    @Override
    public void onDestroy() {
        map_run.onDestroy();
        super.onDestroy();
    }
}
