package com.codeseasy.loginui.cardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codeseasy.loginui.FullscreenActivity;
import com.codeseasy.loginui.R;

import java.util.List;
import java.util.Random;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private static final String TAG = "CardAdapter";

    private Context mContext;

    private List<Device> mDeviceList;
    // 颜色数组 ，用于产生卡片颜色

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        //ImageView cardImage;
        TextView deviceName;
        ImageView deviceImage;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            deviceImage = (ImageView) view.findViewById(R.id.device_bg);
            deviceName = (TextView) view.findViewById(R.id.device_name);

        }
    }

    public DeviceAdapter(List<Device> deviceList) {
        mDeviceList = deviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Device device = mDeviceList.get(position);
                Intent intent = new Intent(mContext, FullscreenActivity.class);
                // 向    FullscreenActivity 传入IP “/192......”
                intent.putExtra(FullscreenActivity.GET_IP, device.getIP());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Device device= mDeviceList.get(position);
        holder.deviceName.setText(device.getName());

        // 设置卡片背景
        Glide.with(mContext).load(device.getImageId()).into(holder.deviceImage);
        //holder.linearLayout.setBackgroundResource(picture[index]);
        //Glide.with(mContext).load(card.getImageId()).into(holder.cardImage);
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }
}
