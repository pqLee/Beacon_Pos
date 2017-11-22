package com.intel.android_beacon.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lpq on 17-10-26.
 * @author lpq
 */
public class Group {
    /*rssi值列表*/
    private List<Integer> rssis = new ArrayList<Integer>();

    public List<Integer> getRssis() {
        return rssis;
    }

    public void setRssis(List<Integer> rssis) {
        this.rssis = rssis;
    }

}
