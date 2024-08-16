package com.example.bluetoothletest.ble;

import android.bluetooth.BluetoothDevice;

public class BluetoothLeDevice {

    public BluetoothLeDevice(String address, String name, int rssi, boolean deviceStatus, String serviceUuid, int type, BluetoothDevice device) {
        this.address = address;
        this.name = name;
        this.rssi = rssi;
        this.deviceStatus = deviceStatus;
        this.serviceUuid = serviceUuid;
        this.type = type;
        this.device = device;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(boolean deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(String serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public String getCharacteristicUuid() {
        return characteristicUuid;
    }

    public void setCharacteristicUuid(String characteristicUuid) {
        this.characteristicUuid = characteristicUuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    private String address;
    private String name;
    private int rssi;
    private boolean deviceStatus;
    private int status;
    private String serviceUuid;
    private String characteristicUuid;
    private int type;
    private BluetoothDevice device;
}
