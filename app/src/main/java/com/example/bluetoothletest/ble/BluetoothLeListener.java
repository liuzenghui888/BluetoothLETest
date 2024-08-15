package com.example.bluetoothletest.ble;
import android.bluetooth.le.ScanResult;

public interface BluetoothLeListener {
    void onBluetoothEnabled(boolean enabled);
    void onAddBluetoothDevice(ScanResult device);
    void onScanLeDeviceStatus(boolean status);
}
