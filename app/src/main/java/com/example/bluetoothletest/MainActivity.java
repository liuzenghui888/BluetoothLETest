package com.example.bluetoothletest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothletest.ble.BluetoothLeDevice;
import com.example.bluetoothletest.ble.BluetoothLeHelper;
import com.example.bluetoothletest.ble.BluetoothLeListener;
import com.example.bluetoothletest.ble.GattAttributes;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity implements BluetoothLeListener {
    private static final int REQUEST_CODE_BLE_PERMISSIONS = 2002;
    private String TAG = "[BLE Demo App] MainActivity";

    private RecyclerView mRecyclerView;
    private DeviceAdapter mDeviceAdapter;
    private Button mBtnHandle;
    private Button mBtnRequest;
    private boolean isScanning = false;
    private boolean isAllPermissionsGranted = false;

    private List<BluetoothLeDevice> mLeDeviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_devicelist);
        mDeviceAdapter = new DeviceAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mDeviceAdapter);

        mBtnHandle = findViewById(R.id.btn_ble_handle);
        mBtnHandle.setOnClickListener(v -> {
            if (isAllPermissionsGranted){
                if (isScanning){
                    BluetoothLeHelper.getInstance(this).stopScanLeDevice();
                } else {
                    BluetoothLeHelper.getInstance(this).startScanLeDevice();
                }
            } else {
                Toast.makeText(this, "请先授予权限", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnRequest = findViewById(R.id.btn_request_device);
        mBtnRequest.setOnClickListener(v -> {
            if(mLeDeviceList != null) {
                mLeDeviceList.clear();
                mDeviceAdapter.setDeviceList(mLeDeviceList);
            }
        });
        requestBluetoothPermissions();
        setmBtnHandleText();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_BLE_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                Log.d(TAG, "allPermissionsGranted");
                isAllPermissionsGranted = true;
                BluetoothLeHelper.getInstance(this).setListener(this).startBleAdvertising();
            }
        }
    }

    private void requestBluetoothPermissions() {
        Log.d(TAG, "requestBluetoothPermissions");
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,},
                REQUEST_CODE_BLE_PERMISSIONS
        );
    }

    @Override
    public void onBluetoothEnabled(boolean enabled) {

    }


    @Override
    public void onAddBluetoothDevice(ScanResult device) {
        Log.d(TAG, "onAddBluetoothDevice");
        if (device != null && device.getScanRecord().getServiceUuids() != null) {
            if (isBluetoothLeDevice(device.getScanRecord().getServiceUuids())) {
                if (!isBluetoothLeRepeat(device.getDevice())) {
                    mLeDeviceList.add(new BluetoothLeDevice(
                            device.getDevice().getName(),
                            device.getDevice().getAddress(),
                            device.getRssi(),
                            "lzh",
                            device.isConnectable(),
                            GattAttributes.LZH_BLE_P2P_SERVICE.toString(),
                            1,
                            device.getDevice()
                            ));
                    mDeviceAdapter.setDeviceList(mLeDeviceList);
                }
            }
        }
    }

    @Override
    public void onScanLeDeviceStatus(boolean status) {
        isScanning = status;
        setmBtnHandleText();
    }

    private void setmBtnHandleText() {
        if (isScanning){
            mBtnHandle.setText("停止扫描");
        } else {
            mBtnHandle.setText("开始扫描");
        }
    }

    //判断设备是否在设备列表
    private boolean isBluetoothLeRepeat(BluetoothDevice device) {
        Log.d(TAG, "isBluetoothLeRepeat : " + device.getName());
        for (BluetoothLeDevice bluetoothLeDevice : mLeDeviceList) {
            if (bluetoothLeDevice.getDevice().getName().equals(device.getName())) {
                return true;
            }
        }
        return false;
    }

    //判断是否需要的设备
    private boolean isBluetoothLeDevice(List<ParcelUuid> serviceUuids) {
        Log.d(TAG, "isBluetoothLeDevice : " + serviceUuids);
        for (ParcelUuid parcelUuid : serviceUuids) {
            if (parcelUuid.equals(ParcelUuid.fromString(String.valueOf(GattAttributes.LZH_BLE_P2P_SERVICE)))) {
                return true;
            }
        }
        return false;
    }
}