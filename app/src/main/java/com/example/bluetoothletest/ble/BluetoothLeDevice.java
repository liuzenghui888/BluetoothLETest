package com.example.bluetoothletest.ble;

import android.bluetooth.BluetoothDevice;

public class BluetoothLeDevice {

    public BluetoothLeDevice(String address, String name, int rssi, String id, boolean status, String serviceUuid, int type, BluetoothDevice device) {
        this.address = address;
        this.name = name;
        this.rssi = rssi;
        this.id = id;
        this.status = status;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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
    private String id;
    private boolean status;
    private String serviceUuid;
    private String characteristicUuid;
    private int type;
    private BluetoothDevice device;
}
