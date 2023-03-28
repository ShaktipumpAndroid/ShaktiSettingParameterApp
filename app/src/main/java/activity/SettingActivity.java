package activity;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vihaan.shaktinewconcept.R;

import webservice.AllPopupUtil;
import webservice.WebURL;

public class SettingActivity extends AppCompatActivity {


    private Context mContext;
    private RecyclerView rclvListViewID ;
    private RelativeLayout rlvFooterID1, rlvFooterID2, rlvFooterID3, rlvFooterID4;

    private ImageView imgInternetToggleID, imgBTToggleID;
    //  private TextView txtTotalEnergyHeadingID, txtConsumptionHeadingID;
    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext =  this;
        initView();
    }

    private void initView() {
        rclvListViewID = (RecyclerView) findViewById(R.id.rclvListViewID);

        rclvListViewID.setHasFixedSize(true);
        rclvListViewID.setLayoutManager(new LinearLayoutManager(this));

        //creating recyclerview adapter
        SettingAdapter adapter = new SettingAdapter(mContext);

        //setting adapter to recyclerview
        rclvListViewID.setAdapter(adapter);



        imgInternetToggleID = (ImageView) findViewById(R.id.imgInternetToggleID);
        imgBTToggleID = (ImageView) findViewById(R.id.imgBTToggleID);

        switch1 = (Switch) findViewById(R.id.switch1);


        onClickEventAll();

    }

    private void onClickEventAll() {


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try {
                    if (isChecked) {
                        imgBTToggleID.setImageResource(R.drawable.iv_bluetooth_selected);
                        imgInternetToggleID.setImageResource(R.drawable.iv_connection_unselected);

                        WebURL.CHECK_FOR_WORK_WITH_BT_OR_IN = 1;

                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter.isEnabled()) {
                                if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                    if (WebURL.BT_DEVICE_NAME.equalsIgnoreCase("") || WebURL.BT_DEVICE_NAME.equalsIgnoreCase(null) || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || WebURL.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                        Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                    ///////////////write the query
                                    //   new BluetoothCommunicationForMotorStop().execute(":TURNON#", ":TURNON#", "START");
                                } else {
                                    // AllPopupUtil.btPopupCreateShow(mContext);
                                    mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                                }
                            } else {
                                //  AllPopupUtil.btPopupCreateShow(mContext);
                                mContext.startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                            }

                        // switch1.setChecked(false);
                    } else {
                        imgBTToggleID.setImageResource(R.drawable.iv_bluetooth_unselected);
                        imgInternetToggleID.setImageResource(R.drawable.iv_connection_selected);

                        WebURL.BT_DEVICE_NAME = "";
                        WebURL.BT_DEVICE_MAC_ADDRESS = "";
                        WebURL.CHECK_FOR_WORK_WITH_BT_OR_IN = 0;
                        // switch1.setChecked(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
