package com.example.xdonor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BloodRequestViewAdapter extends RecyclerView.Adapter<BloodRequestViewAdapter.BloodRequestViewHolder> {
    ArrayList<BloodReqiestViewModel> bloodReqiestViewModelArrayList;
    Context context;

    public BloodRequestViewAdapter(ArrayList<BloodReqiestViewModel> bloodReqiestViewModelArrayList, Context context) {
        this.bloodReqiestViewModelArrayList = bloodReqiestViewModelArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public BloodRequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_blood_request, parent, false);
        return new BloodRequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull BloodRequestViewHolder holder, int position) {
        String imgurl=bloodReqiestViewModelArrayList.get(position).getImage();
        Picasso.get().load(imgurl).into(holder.profile);
        holder.name.setText(bloodReqiestViewModelArrayList.get(position).getName());
        holder.address.setText(bloodReqiestViewModelArrayList.get(position).getAddress());
        holder.hospital.setText(bloodReqiestViewModelArrayList.get(position).getHospital());
        String blood = bloodReqiestViewModelArrayList.get(position).getBloodGroup();
        String blood_group = "";
        String blood_sing = "";
        if (blood.length() == 2) {
            blood_group = String.valueOf(blood.charAt(0));
            blood_sing = String.valueOf(blood.charAt(1));
        } else if (blood.length() == 3) {
            blood_group += String.valueOf(blood.charAt(0));
            blood_group += String.valueOf(blood.charAt(1));
            blood_sing = String.valueOf(blood.charAt(2));
        }
        holder.bloodGroup.setText(blood_group);

        if (blood_sing.equals("+"))
            holder.bloodSing.setText("(Positive)");
        else
            holder.bloodSing.setText("(Negative)");

        if (bloodReqiestViewModelArrayList.get(position).getEmergency().equals("emergency")) {
            holder.emergencyLyt.setVisibility(View.VISIBLE);
            holder.blood_request_lyt.setBackground(context.getResources().getDrawable(R.drawable.cardshadow3));
//        holder.blood_request_lyt.setBackgroundColor(Color.parseColor("#ffb7b7"));
        } else holder.emergencyLyt.setVisibility(View.GONE);
        holder.blood_request_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new RequestViewFragment()).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bloodReqiestViewModelArrayList.size();
    }

    public class BloodRequestViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, bloodGroup, bloodSing, address, hospital;
        LinearLayout emergencyLyt, blood_request_lyt;

        public BloodRequestViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.bloodRequestImage);
            name = itemView.findViewById(R.id.bloodRequestName);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
            bloodSing = itemView.findViewById(R.id.bloodGroupSing);
            address = itemView.findViewById(R.id.location);
            hospital = itemView.findViewById(R.id.hospital);
            emergencyLyt = itemView.findViewById(R.id.lyt_emergency);
            blood_request_lyt = itemView.findViewById(R.id.blood_request_lyt);

        }
    }
}
