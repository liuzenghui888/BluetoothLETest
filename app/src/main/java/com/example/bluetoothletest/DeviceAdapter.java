package com.example.bluetoothletest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothletest.ble.BluetoothLeDevice;
import com.example.bluetoothletest.ble.BluetoothLeHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressLint("MissingPermission")
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>{
    private String TAG = "[BLE Demo App] DeviceAdapter";
    private List<BluetoothLeDevice> mLeDeviceList = new ArrayList<>();
    private Context mContext;

    private static final int BLE_DEVICE_DISCONNECT = 0;
    private static final int BLE_DEVICE_CONNECT = 1;
    private static final int BLE_DEVICE_INVITED = 2;

    public DeviceAdapter(Context context) {
        mContext = context;
    }

    public void setDeviceList(Collection<BluetoothLeDevice> leDeviceList) {
        Log.d(TAG, "setDeviceList");
        this.mLeDeviceList.clear();
        if (mLeDeviceList != null) {
            Log.d(TAG, "setDeviceList: " + leDeviceList.size());
            this.mLeDeviceList.addAll(leDeviceList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_deviceName.setText(mLeDeviceList.get(position).getDevice().getName());
        holder.tv_deviceAddress.setText(mLeDeviceList.get(position).getDevice().getAddress());
        holder.tv_deviceRssi.setText(mLeDeviceList.get(position).getRssi() + "dB");
        if (mLeDeviceList.get(position).getStatus() == BLE_DEVICE_DISCONNECT) {
            holder.tv_status.setText("未连接");
        } else if (mLeDeviceList.get(position).getStatus() == BLE_DEVICE_CONNECT) {
            holder.tv_status.setText("已连接");
        } else if (mLeDeviceList.get(position).getStatus() == BLE_DEVICE_INVITED) {
            holder.tv_status.setText("连接中");
        }
        holder.btn_handle.setText("连接");

        holder.btn_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_status.setText("连接中");
                BluetoothLeHelper.getInstance(mContext).connectBluetoothGatt(mLeDeviceList.get(position).getDevice().getAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLeDeviceList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_deviceName;
        private TextView tv_deviceAddress;
        private TextView tv_status;
        private TextView tv_deviceRssi;
        private Button btn_handle;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_deviceAddress = itemView.findViewById(R.id.tv_device_ip);
            tv_deviceName = itemView.findViewById(R.id.tv_device_name);
            tv_deviceRssi = itemView.findViewById(R.id.tv_device_rssi);
            tv_status = itemView.findViewById(R.id.tv_status);
            btn_handle = itemView.findViewById(R.id.btn_handle);
        }
    }
}