package com.intel.android_beacon.pos_algorithm;

import com.intel.android_beacon.bean.LocationUtil;

import org.altbeacon.beacon.Beacon;

import java.util.List;

/**
 * Created by lpq on 17-10-26.
 * 定位算法的父接口
 * @author lpq
 */

public interface Dealer {
    /**
     * 待定位终端的坐标\
     *
     * @param uniqueBases 接收到的一组基站信息(包括自身ID,强度值信号)组成的字符串(格式为“id,rssi;id,rssi........id,rssi;terminalID”)
     * @return 返回定位结果对象
     */
    //public LocationUtil getLocation(String str);
    public LocationUtil getLocation(List<Beacon> uniqueBases);
}
