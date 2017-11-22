package com.intel.android_beacon.pos_manage;

import com.intel.android_beacon.bean.LocationUtil;
import com.intel.android_beacon.pos_algorithm.Dealer;
import com.intel.android_beacon.pos_algorithm.Trilateral;
import com.intel.android_beacon.util_manager.CopyOnWriteMap;

import org.altbeacon.beacon.Beacon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by lpq on 17-10-26.
 *
 * 定位逻辑实现.
 *
 * UUID：厂商识别号
 * Major：相当于群组号，同一个组里Beacon有相同的Major
 * Minor：相当于识别群组里单个的Beacon
 * TX Power：用于测量设备离Beacon的距离
 *
 * UUID+Major+Minor就构成了一个Beacon的识别号，有点类似于网络中的IP地址。
 *
 * @author lpq
 */

public class BLELocation {

    /*定位算法*/
    public static Dealer dealer;

    /*基站坐标的查询缓存*/
    public static Map<String, Double[]> baseStationLocs;

    public LocationUtil getLocationID(List<Beacon> bases)
    {
        LocationUtil location;
        baseStationLocs = new CopyOnWriteMap<>();

        dealer = new Trilateral();

        initDatas();
        location = dealer.getLocation(bases);

        return location;
    }

    private void initDatas()
    {
        baseStationLocs.put("fda50693-a4e2-4fb1-afcf-c6eb07647824", new Double[]{0.0, 0.0});
        baseStationLocs.put("fda50693-a4e2-4fb1-afcf-c6eb07647825", new Double[]{4.6, 0.0});
        baseStationLocs.put("fda50693-a4e2-4fb1-afcf-c6eb07647826", new Double[]{4.6, 3.6});
    }
}
