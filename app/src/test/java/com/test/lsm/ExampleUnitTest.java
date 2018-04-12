package com.test.lsm;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.test.lsm.global.Const;
import com.test.lsm.utils.BaiduMapUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    private static final String TAG = "ExampleUnitTest";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testCalc(){

        List<LatLng> latLngs = new ArrayList<>();

        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);

        for (int i = 0; i < Const.googleWGS84.length; i++) {
            String[] ll = Const.googleWGS84[i].split(",");
            LatLng sourceLatLng = new LatLng(Double.valueOf(ll[0]), Double.valueOf(ll[1]));
            converter.coord(sourceLatLng);
            LatLng desLatLng = converter.convert();
            latLngs.add(desLatLng);
        }

        double distance = BaiduMapUtils.calcDistance(latLngs);
        Log.e(TAG, "testCalc: "+distance );

    }



}