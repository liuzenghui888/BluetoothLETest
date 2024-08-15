package com.example.bluetoothletest.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.RequiresApi;

@SuppressLint("MissingPermission")
public class BluetoothLeHelper {
    private Context mContext;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager mBluetoothManager;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeListener mBluetoothLeListener;

    private static volatile BluetoothLeHelper instance;
    private String TAG = "[BLE Demo App] BluetoothLeHelper";

    public synchronized static BluetoothLeHelper getInstance(Context context){
        if(instance == null){
            instance = new BluetoothLeHelper(context);
        }
        return instance;
    }

    public BluetoothLeHelper(Context context) {
        Log.d(TAG, "BluetoothLeHelper");
        mContext = context.getApplicationContext();
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            mBluetoothGattServer = mBluetoothManager.openGattServer(mContext, mGattServerCallback);
            initBluetoothGattServer();
        }
    }

    public BluetoothLeHelper setListener(BluetoothLeListener bluetoothLeListener) {
        Log.d(TAG, "setListener");
        mBluetoothLeListener = bluetoothLeListener;
        return this;
    }

    public void startScanLeDevice() {
        Log.d(TAG, "startScanLeDevice");
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothLeScanner.startScan(mScanCallback);
            mBluetoothLeListener.onScanLeDeviceStatus(true);
        }
    }

    public void stopScanLeDevice() {
        Log.d(TAG, "stopScanLeDevice");
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothLeScanner.stopScan(mScanCallback);
            mBluetoothLeListener.onScanLeDeviceStatus(false);
        }
    }

    private ScanCallback mScanCallback = new ScanCallback(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result){
            super.onScanResult(callbackType, result);
            if (mBluetoothLeListener != null) {
                mBluetoothLeListener.onAddBluetoothDevice(result);
            }
        }
    };

    private final BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            Log.d(TAG, "BluetoothGattServerCallback onConnectionStateChange");
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "BluetoothGattServerCallback onCharacteristicReadRequest");
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            Log.d(TAG, "BluetoothGattServerCallback onCharacteristicWriteRequest");
            characteristic.setValue(value);
            if (responseNeeded) {
                mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
            }
        }
    };

    public void connectBluetoothGatt(String address) {
        Log.d(TAG, "connectBluetoothGatt : " + address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "BluetoothGattCallback onConnectionStateChange");
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "BluetoothGattCallback onServicesDiscovered");

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d(TAG, "BluetoothGattCallback onCharacteristicRead");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "BluetoothGattCallback onCharacteristicChanged");
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void startBleAdvertising(){
        Log.d(TAG, "startBleAdvertising");
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

            AdvertiseSettings settings = new AdvertiseSettings.Builder()
                    .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                    .setTimeout(0)
                    .setConnectable(true)
                    .build();

            AdvertiseData data = new AdvertiseData.Builder()
                    .setIncludeDeviceName(true)
                    .addServiceUuid(new ParcelUuid(GattAttributes.LZH_BLE_P2P_SERVICE)) // 必须有一个服务 UUID
                    .build();

            advertiser.startAdvertising(settings, data, mAdvertiseCallback);
        }
    }

    private final AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.d(TAG, "AdvertiseCallback onStartSuccess");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.d(TAG, "AdvertiseCallback onStartFailure");
        }
    };

    public void initBluetoothGattServer() {
        Log.d(TAG, "initBluetoothGattServer");
        BluetoothGattService service = new BluetoothGattService(GattAttributes.LZH_BLE_P2P_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(
                GattAttributes.LZH_BLE_P2P_ATTR,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ | BluetoothGattCharacteristic.PERMISSION_WRITE
        );
        service.addCharacteristic(characteristic);
        mBluetoothGattServer.addService(service);
    }

    //需要系统权限
    public String getMyDeviceAddress(){
        Log.d(TAG, "getMyDeviceAddress");
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return mBluetoothAdapter.getAddress();
        } else {
            return "";
        }
    }

    public boolean sendNotification(BluetoothDevice device, String message){
        Log.d(TAG, "sendNotification : device : " + device + " message : " + message);
        BluetoothGattCharacteristic characteristic = mBluetoothGattServer
                .getService(GattAttributes.LZH_BLE_P2P_SERVICE)
                .getCharacteristic(GattAttributes.LZH_BLE_P2P_ATTR);

        characteristic.setValue(message);

        return mBluetoothGattServer.notifyCharacteristicChanged(device, characteristic, false);
    }

    public void clear() {
        Log.d(TAG, "clear");
        if (mBluetoothGatt != null){
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        if (mBluetoothGattServer != null){
            mBluetoothGattServer.close();
            mBluetoothGattServer = null;
        }
    }
}