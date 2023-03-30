package activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.shakti.shaktinewconcept.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairedDeviceActivity extends AppCompatActivity {


    private Context mContext ;
    private RecyclerView rclSettingListViewID;
    private LinearLayoutManager lLayout;
    private RecyclerView.Adapter recyclerViewAdapter;

    private TextView txtPairedDeviceListID;

    private RelativeLayout rlvBackViewID;

    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_device);

        mContext = this;
        initView();
    }

    @SuppressLint("MissingPermission")
    private void initView() {

        rlvBackViewID = (RelativeLayout) findViewById(R.id.rlvBackViewID);

        rclSettingListViewID = (RecyclerView) findViewById(R.id.rclSettingListViewID);
        txtPairedDeviceListID = (TextView) findViewById(R.id.txtPairedDeviceListID);

        lLayout = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rclSettingListViewID.setNestedScrollingEnabled(false);
        rclSettingListViewID.setLayoutManager(lLayout);

        try {
            bAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bAdapter == null) {
                Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
                finish();
            } else if (!bAdapter.isEnabled()) {
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtPairedDeviceListID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDeviceList();
            }
        });


        rlvBackViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    pairedDeviceList();


     /*   if(mSettingModelResponse != null && mSettingModelResponse.size() > 0)
            mSettingModelResponse.clear();

        mSettingModelResponse = mSettingModelView.getResponse();

        if (recyclerViewAdapter != null)
            recyclerViewAdapter = null;

        // recyclerViewAdapter = new HomeOrderAdapter("sss",getActivity());
        recyclerViewAdapter = new DeviceSettingAdapter(mContext, mSettingModelResponse);

        rclSettingListViewID.setAdapter(recyclerViewAdapter);*/
    }

    private void pairedDeviceList() {

        if(bAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else{
            @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
            List mDeviceNameList = new ArrayList();
            List mDeviceMACAddressList = new ArrayList();
            if(pairedDevices.size()>0){
                for(BluetoothDevice device: pairedDevices){
                    @SuppressLint("MissingPermission") String devicename = device.getName();
                    String macAddress = device.getAddress();
                    mDeviceNameList.add(devicename);
                    //  mDeviceNameList.add("Name: "+devicename);
                    // mDeviceMACAddressList.add("MAC Address: "+macAddress);
                    mDeviceMACAddressList.add(macAddress);
                }
                if (recyclerViewAdapter != null)
                    recyclerViewAdapter = null;

                // recyclerViewAdapter = new HomeOrderAdapter("sss",getActivity());
                recyclerViewAdapter = new BTPairedDeviceAdapter(mContext, mDeviceNameList,mDeviceMACAddressList);

                rclSettingListViewID.setAdapter(recyclerViewAdapter);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
