package com.example.xdonor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    //initialized variable
    private ArrayList<ActionLogModel> actionLogModelArrayList;
    private RecyclerView actionLogView;
    private ImageView regProfileImage;
    private TextView name, details, location, blood_group;
    //initialized firebase variable
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        assignVariable(view);
        setLogData();
        return view;
    }

    /**
     * assign variable
     * * @param view
     */
    private void assignVariable(View view){
        //assing ui variables
        actionLogView = view.findViewById(R.id.LogView);
        regProfileImage = view.findViewById(R.id.regProfileImage);
        name = view.findViewById(R.id.name);
        details = view.findViewById(R.id.details);
        location = view.findViewById(R.id.location);
        blood_group = view.findViewById(R.id.blood_group);
        //assign firebase variable
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //store data in UI variables
        setData();
    }

    /**
     * set UI variables data from database
     */
    private void setData() {
        userId = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                String address="";
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Picasso.get()
                            .load(document.get("profilePic").toString())
                            .placeholder(R.drawable.baseline_account_circle_black_48dp)
                            .error(R.drawable.baseline_account_circle_black_48dp)
                            .into(regProfileImage);
                    name.setText(document.get("name").toString());
                    details.setText(document.get("details").toString());
                    address +=document.get("policeStation").toString()+", ";
                    address+=document.get("district").toString()+" - ";
                    address+=document.get("postOffice").toString();
                    location.setText(address);
                    blood_group.setText(document.get("bloodGroup").toString());

                }
            }
        });
    }

    private void setLogData() {
        if(mAuth.getCurrentUser() != null){
            actionLogView.setLayoutManager(new LinearLayoutManager(getContext()));
            actionLogModelArrayList = new ArrayList<>();
            //actionLogView.setAdapter(new ActionLogAdapter(actionLogModelArrayList));
            firebaseFirestore.collection("Users").document(userId)
                    .collection("activity").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (!document.getId().toString().equals("initial")){
                                            actionLogModelArrayList.add(new ActionLogModel(
                                                    document.get("date").toString(),
                                                    document.get("time").toString(),
                                                    document.get("activity").toString()
                                            ));
                                    }
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                }
                                actionLogView.setAdapter(new ActionLogAdapter(actionLogModelArrayList));

                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}