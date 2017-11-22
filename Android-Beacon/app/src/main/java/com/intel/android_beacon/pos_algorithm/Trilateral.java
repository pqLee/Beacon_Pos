package com.intel.android_beacon.pos_algorithm;

import android.util.Log;

import com.intel.android_beacon.bean.LocationUtil;
import com.intel.android_beacon.pos_manage.BLELocation;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * Created by lpq on 17-10-26.
 *
 * 三边定位算法
 * @author lpq
 */

public class Trilateral implements Dealer{
    /**
     * 格式为“id,rssi;id,rssi........id,rssi;terminalID”
     */

    /*定位结果*/
    private LocationUtil location;

    /** 更新是不断调用该API即可
     * baseStationLocs: 查询数据库得到的数据
    */
    @Override
    public LocationUtil getLocation(List<Beacon> uniqueBases) {
        	/*实例化定位结果*/
        location = new LocationUtil();
        /**
         * 初始化
         */

        return calculate(uniqueBases);
    }

    /**
     * 计算定位坐标
     *
     * @param bases 接收到的一组基站k对象列表(此处列表中的基站应当是id各异的)
     * @return  返回定位坐标
     */
    public LocationUtil calculate(List<Beacon> bases){
        int baseNum = bases.size();    // 基站数量

        /*距离数组*/
        double[] distanceArray = new double[baseNum];

        String[] id_s = new String[baseNum];

        int j = 0;

		/*获得基站id*/
        for (Beacon base : bases) {
            // distanceArray[j] = base.getDistance(height, n, p0);  //考虑环境因素因子的公式.
            distanceArray[j] = base.getDistance();
            /*** 设置基站的id ***/
            id_s[j] = base.getId1().toString();     // 赋予基站的ID
            j++;
        }

        int disArrayLength = distanceArray.length;

        double[][] a = new double[baseNum-1][2];

        double[][] b = new double[baseNum-1][1];

        /**
         * 数组a初始化
         */
        for (int i = 0; i < 2; i++)
        {
            a[i][0] = 2 * (BLELocation.baseStationLocs.get(id_s[i])[0] - BLELocation.baseStationLocs .get(id_s[baseNum-1])[0]);
            //Log.d("Matrix a[i][0] : ", String.valueOf(a[i][0]));
            a[i][1] = 2 * (BLELocation.baseStationLocs.get(id_s[i])[1] - BLELocation.baseStationLocs .get(id_s[baseNum-1])[1]);
            //Log.d("Matrix a[i][1] : ", String.valueOf(a[i][1]));
        }

        /**
         * 数组b初始化
         */
        for(int i = 0; i < 2; i ++ ) {
            b[i][0] = Math.pow(BLELocation.baseStationLocs.get(id_s[i])[0], 2)
                    - Math.pow(BLELocation.baseStationLocs.get(id_s[baseNum-1])[0], 2)
                    + Math.pow(BLELocation.baseStationLocs.get(id_s[i])[1], 2)
                    - Math.pow(BLELocation.baseStationLocs.get(id_s[baseNum-1])[1], 2)
                    + Math.pow(distanceArray[disArrayLength-1], 2)
                    - Math.pow(distanceArray[i],2);
        }


        /*将数组封装成矩阵*/
        Matrix b1 = new Matrix(b);
        Matrix a1 = new Matrix(a);

		/*求矩阵的转置*/
        Matrix a2 = a1.transpose();

		/*求矩阵a1与矩阵a1转置矩阵a2的乘积*/
        Matrix tmpMatrix1 = a2.times(a1);
        Matrix reTmpMatrix1 = tmpMatrix1.inverse();   //奇异矩阵无逆矩阵
        Matrix tmpMatrix2 = reTmpMatrix1.times(a2);
        //Matrix tmpMatrix2 = tmpMatrix1.times(a2);

		/*中间结果乘以最后的b1矩阵*/
        Matrix resultMatrix = tmpMatrix2.times(b1);
        double[][] resultArray = resultMatrix.getArray();

        location.setxAxis(resultArray[0][0]);
        location.setyAxis(resultArray[1][0]);

        return location;
    }
}
