package com.example.bluetoothletest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

    public DeviceAdapter(Context context) {
        mContext = context;
    }

    public void setDeviceList(Collection<BluetoothLeDevice> leDeviceList) {
        Log.d(TAG, "setDeviceList");
        this.mLeDeviceList.clear();
        if (mLeDeviceList != null) {
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
        holder.tv_deviceStatus.setText("可用");
        holder.btn_handle.setText("连接");

        holder.btn_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothLeHelper.getInstance(mContext).connectBluetoothGatt(mLeDeviceList.get(position).getDevice().getAddress());
                holder.ll_btn_send.setVisibility(View.VISIBLE);
            }
        });

        holder.btn_send.setText("发送");
        holder.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BluetoothLeHelper.getInstance(mContext).sendNotification(mLeDeviceList.get(position).getDevice(), "Hello World");
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
        private TextView tv_deviceStatus;
        private LinearLayout ll_btn_send;
        private Button btn_send;
        private Button btn_handle;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_deviceAddress = itemView.findViewById(R.id.tv_device_ip);
            tv_deviceName = itemView.findViewById(R.id.tv_device_name);
            tv_deviceStatus = itemView.findViewById(R.id.tv_device_status);
            ll_btn_send = itemView.findViewById(R.id.ll_btn_send);
            btn_handle = itemView.findViewById(R.id.btn_handle);
            btn_send = itemView.findViewById(R.id.btn_send);
        }
    }
}