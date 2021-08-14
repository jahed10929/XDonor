package com.example.xdonor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DonationViewAdapter extends RecyclerView.Adapter<DonationViewAdapter.ViewHolder> {
    ArrayList<DonationViewModel>donationViewModelArrayList;

    public DonationViewAdapter(ArrayList<DonationViewModel> donationViewModelArrayList) {
        this.donationViewModelArrayList = donationViewModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_itame_view, parent, false);
        return new DonationViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.year.setText(donationViewModelArrayList.get(position).getYear());
        holder.month.setText(donationViewModelArrayList.get(position).getDate());
        holder.day.setText(donationViewModelArrayList.get(position).getTime());
        holder.location.setText(donationViewModelArrayList.get(position).getLocation());
        holder.hoslpital.setText(donationViewModelArrayList.get(position).getHospital());
        holder.bloodGroup.setText(donationViewModelArrayList.get(position).getBloodgroup());
        holder.lastDonation.setText(donationViewModelArrayList.get(position).getLastDonation()+" Days Ago");
    }

    @Override
    public int getItemCount() {
        return donationViewModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView year, month, day, location, hoslpital, bloodGroup, lastDonation;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.year);
            month = itemView.findViewById(R.id.month);
            day = itemView.findViewById(R.id.day);
            location = itemView.findViewById(R.id.location);
            hoslpital = itemView.findViewById(R.id.hospital);
            bloodGroup = itemView.findViewById(R.id.blood_group);
            lastDonation = itemView.findViewById(R.id.lastDonateDate);
        }
    }
}
