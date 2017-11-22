package com.intel.android_beacon.util_manager;

import com.intel.android_beacon.bean.LocationUtil;
import com.intel.android_beacon.pos_algorithm.Dealer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Created by lpq on 17-10-26.
 * @author lpq
 */
public class Server {

    /**
     * 数据格式: id,rssi;id,rssi........id,rssi;terminalID
     */

    /*定位结果队列*/
    public static BlockingQueue<LocationUtil> locs;

    /*定位算法*/
    public static Dealer dealer;

    /*基站坐标的查询缓存*/
    public static Map<String, Double[]> baseStationLocs;

    /*环境因子的查询缓存*/
    public static Map<Integer, Double[]> envFactors;

}
