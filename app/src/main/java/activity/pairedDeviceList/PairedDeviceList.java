package activity.pairedDeviceList;

import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vihaan.shaktinewconcept.R;

import java.util.ArrayList;
import java.util.Set;

import Database.DatabaseHelper;
import activity.DeviceOnOffActivity;
import activity.ScannedBarcodeActivity;
import activity.devicecomponetelist.DeviceComponentList;
import activity.devicecomponetelist.model.PairDeviceModel;
import activity.utility.CustomUtility;
import adapter.PairedDeviceAdapter;
import webservice.AllPopupUtil;
import webservice.WebURL;

public class PairedDeviceList extends AppCompatActivity implements PairedDeviceAdapter.deviceSelectionListener {
    LinearLayout bluetoothLayout;
    ArrayList<PairDeviceModel> pairedDeviceList = new ArrayList<>();
    RecyclerView bluetoothDeviceList;
    ImageView imgLogoID;
    TextView bluetoothState;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_CODE_PERMISSION = 1;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_paried_device_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0,systemBars.top,0,0);
            return insets;
        });

        inIt();
        listener();
    }

    private void inIt() {
        databaseHelper = new DatabaseHelper(this);
        bluetoothDeviceList = findViewById(R.id.bluetoothDeviceList);
        bluetoothState = findViewById(R.id.bluetoothState);
        imgLogoID = findViewById(R.id.imgLogoID);
        bluetoothLayout = findViewById(R.id.bluetoothLayout);

        if (!checkPermission()) {
            requestPermission();
        } else {
            registerBroadcastManager();
        }

    }

    private void listener() {
        imgLogoID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PairedDeviceList.this)
                        .setTitle(R.string.sign_out)
                        .setMessage(R.string.sign_out_application)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            CustomUtility.clearSharedPreferences(getApplicationContext());
                            databaseHelper.deleteDatabase();
                            dialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), ScannedBarcodeActivity.class);
                            startActivity(intent1);
                            finish();

                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> {
                            // user doesn't want to logout

                            dialog.dismiss();
                        })
                        .show();

            }
        });
    }

    private final BroadcastReceiver mBluetoothStatusChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        bluetoothDeviceOff(getResources().getString(R.string.bluetooth_is_off));
                        break;

                    case BluetoothAdapter.STATE_ON:

                        getPairedDeviceList();

                        break;

                }

            }
        }
    };

    private void registerBroadcastManager() {
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStatusChangedReceiver, filter1);

        if (CustomUtility.isBluetoothEnabled()) {
            bluetoothLayout.setVisibility(View.GONE);
            getPairedDeviceList();

        } else {
            bluetoothDeviceOff(getResources().getString(R.string.bluetooth_is_off));
        }

    }

    private void bluetoothDeviceOff(String message) {
        bluetoothDeviceList.setVisibility(View.GONE);
        bluetoothLayout.setVisibility(View.VISIBLE);
        bluetoothState.setText(message);
        pairedDeviceList = new ArrayList<>();
    }

    private void getPairedDeviceList() {
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {

                for (BluetoothDevice device : pairedDevices) {
                    pairedDeviceList.add(new PairDeviceModel(device.getName(), device.getAddress(), false));
                }
                bluetoothDeviceList.setVisibility(View.VISIBLE);
                bluetoothLayout.setVisibility(View.GONE);
                PairedDeviceAdapter pairedDeviceAdapter = new PairedDeviceAdapter(this, pairedDeviceList);
                bluetoothDeviceList.setAdapter(pairedDeviceAdapter);
                pairedDeviceAdapter.deviceSelection(this);
            } else {
                bluetoothDeviceOff(getResources().getString(R.string.no_paired_device));
            }
        }
    }

    @Override
    public void DeviceSelectionListener(PairDeviceModel pairDeviceModel, int position) {
        Intent i = new Intent(PairedDeviceList.this, DeviceComponentList.class);
        WebURL.BT_DEVICE_MAC_ADDRESS = pairDeviceModel.getDeviceAddress();
        startActivity(i);
    }

    private boolean checkPermission() {
        int Bluetooth = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int BluetoothConnect = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH_CONNECT);
        int BluetoothScan = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH_SCAN);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        if (SDK_INT >= Build.VERSION_CODES.S) {
            return Bluetooth == PackageManager.PERMISSION_GRANTED && BluetoothConnect == PackageManager.PERMISSION_GRANTED
                    && BluetoothScan == PackageManager.PERMISSION_GRANTED;
        } else {
            return Bluetooth == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;

        }

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this,
                    new String[]{BLUETOOTH_CONNECT, BLUETOOTH_SCAN},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{BLUETOOTH, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {

                if (SDK_INT >= Build.VERSION_CODES.S) {

                    boolean BluetoothConnect = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean BluetoothScan = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (BluetoothConnect && BluetoothScan) {
                        registerBroadcastManager();
                    } else {
                        requestPermission();
                    }
                } else {

                    boolean Bluetooth = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (Bluetooth && ReadExternalStorage && WriteExternalStorage) {
                        registerBroadcastManager();
                    } else {
                        requestPermission();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothStatusChangedReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBluetoothStatusChangedReceiver);
        }
    }

}