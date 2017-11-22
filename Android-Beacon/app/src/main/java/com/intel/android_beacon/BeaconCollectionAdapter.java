package com.intel.android_beacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class BeaconCollectionAdapter extends BaseAdapter {

    public class BeaconComparator implements Comparator<Beacon> {
        @Override
        public int compare(Beacon beacon1, Beacon beacon2) {
            return Double.compare(beacon1.getDistance(), beacon2.getDistance());
        }
    }

    private ArrayList<Beacon> beacons;
    private LayoutInflater inflater;

    public BeaconCollectionAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.beacons = new ArrayList<Beacon>();
    }

    public void replaceWith(Collection<Beacon> beacons) {
        this.beacons.clear();
        this.beacons.addAll(beacons);
        Collections.sort(this.beacons, new BeaconComparator());   //按照距离远近排序
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return beacons.size();
    }

    @Override
    public Beacon getItem(int position) {
        return beacons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = inflater.inflate(R.layout.list_item, null);
        }

        Beacon beacon = getItem(position);
        TextView mac = (TextView) v.findViewById(R.id.macAddress);
        TextView uuid = (TextView) v.findViewById(R.id.uuidNumber);
        TextView major = (TextView) v.findViewById(R.id.majorNumber);
        TextView minor = (TextView) v.findViewById(R.id.minorNumber);
        TextView distance = (TextView) v.findViewById(R.id.distance);

        mac.setText(beacon.getBluetoothAddress());
        uuid.setText(beacon.getId1().toString());
        major.setText(beacon.getId2().toString());
        minor.setText(beacon.getId3().toString());
        distance.setText(new DecimalFormat("#0.000").format(beacon.getDistance()));

        return v;
    }
}
