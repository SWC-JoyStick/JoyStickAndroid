package com.codeseasy.loginui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codeseasy.loginui.cardView.Device;

import java.util.List;

public class switchAdapter extends RecyclerView.Adapter<switchAdapter.ViewHolder> {

    private Context mContext;
    private List<Device> mdeviceList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
        }
    }

    public switchAdapter(List<Device> deviceList){
        mdeviceList = deviceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_button,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){

    }

    @Override
    public int getItemCount(){
        return mdeviceList.size();
    }
}
