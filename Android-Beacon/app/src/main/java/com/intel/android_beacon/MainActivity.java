package com.intel.android_beacon;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.android_beacon.bean.LocationUtil;
import com.intel.android_beacon.pos_manage.BLELocation;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager;
    private TextView statusView;
    private TextView positionView;
    private BeaconCollectionAdapter beaconAdapter;

    private List<Beacon> basesList;
    private BLELocation mBLELocation;
    private LocationUtil mLocationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        this.beaconAdapter = new BeaconCollectionAdapter(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        this.statusView = (TextView) findViewById(R.id.currentStatus);
        this.positionView = (TextView)findViewById(R.id.position);
        listView.setAdapter(this.beaconAdapter);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        mBLELocation = new BLELocation();
        mLocationUtil = new LocationUtil();
        basesList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException ignored) {   }

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> rangedBeacons, Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusView.setText("Beacons found: " + rangedBeacons.size());





                        for (Beacon beacons : rangedBeacons) {
                            //Log.d("distance : ", String.valueOf(beacons.getDistance()));
                            //Log.d("uuid : ", String.valueOf(beacons.getId1()));

                            basesList.add(beacons);
                        }
                        if (basesList.size() >= 3) {
                            Log.e("List size : ", String.valueOf(basesList.size()));
                            mLocationUtil = mBLELocation.getLocationID(basesList);
                            Log.d("x axis : ", String.valueOf(mLocationUtil.getxAxis()));
                            Log.d("y axis : ", String.valueOf(mLocationUtil.getyAxis()));
                            positionView.setText("x : " + mLocationUtil.getxAxis() + '\n'
                                + "y : " + mLocationUtil.getyAxis());
                        }

                        basesList.clear();





                        //beaconAdapter.replaceWith(rangedBeacons);
                    }
                });
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException ignored) {   }
    }
}