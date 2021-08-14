package com.example.xdonor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestViewFragment extends Fragment {
    //initialized variables
    private TextView btnCalltNow, btnYesIam, number;
    private static final int REQUEST_CALL = 1;
    ProgressBar progressBar;
    FragmentManager fragmentManager;
    //initialized firebase variable
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    public RequestViewFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_request_view, container, false);
        assignVariable(view);
        return view;
    }

    /**
     * Assign UI Variables
     * @param view
     */
    private void assignVariable(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        number = view.findViewById(R.id.number);
        btnCalltNow = view.findViewById(R.id.btnCalltNow);
        btnYesIam = view.findViewById(R.id.btnYesIam);
        //assign firebase variable
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //handle call noe button click
        btnCalltNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when clicked in button make a phone call
                makePhoneCall();
            }
        });
        //if this button clicked
        //then this will sat as blood donate
        //and store this data to activityLog in database
        btnYesIam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeData();
            }
        });
    }

    /**
     * store blood donat data in user activity
     */
    private void storeData() {
        progressBar.setVisibility(View.VISIBLE);
        userId = mAuth.getCurrentUser().getUid();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        DocumentReference documentReference = firebaseFirestore
                .collection("Users").document(userId)
                .collection("activity").document();
        Map<String, Object> activityLog = new HashMap<>();
        activityLog.put("date", currentDate);
        activityLog.put("time", currentTime);
        activityLog.put("activity", "Blood Donated");
        documentReference.set(activityLog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                Toast.makeText(getContext(), "blood donate Successfull", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new HomeFragment()).addToBackStack(null).commit();
            }
        });
    }

    private void makePhoneCall() {
        String phone_number = number.getText().toString();
        if (phone_number.trim().length()>0){
            //check this apps have permission for make call or not
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                //if don't have permission
                // then again ask for phone call permission
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }else {
                //if permission granted the start call
                String dial = "tel:"+phone_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }else {
            Toast.makeText(getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == REQUEST_CALL){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }
            else Toast.makeText(getContext(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
        }
    }
}