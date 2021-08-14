package com.example.xdonor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HomeFragment extends Fragment {
    //blood rerueast api
    private String url = "https://jsonkeeper.com/b/0BT7";
    ProgressBar progressBar;
    LinearLayout linearLayout;
    ScrollView scrollView;
    Button postBloodRequest;
    TextView bloodDonatTime,donateConfirmation,myBloodGroup,bloodSearchingAmount;
    RecyclerView bloodRequestView;
    ArrayList<BloodReqiestViewModel> bloodReqiestViewModels;
    AppCompatActivity activity;
    private RequestQueue requestQueue;
    //initialized firebase variable
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private String blood;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        postBloodRequest = view.findViewById(R.id.btnPostNow);
        progressBar = view.findViewById(R.id.progressBar);
        bloodDonatTime = view.findViewById(R.id.bloodDonatTime);
        donateConfirmation = view.findViewById(R.id.donateConfirmation);
        myBloodGroup = view.findViewById(R.id.myBloodGroup);
        bloodSearchingAmount = view.findViewById(R.id.bloodSearchingAmount);
        bloodRequestView = view.findViewById(R.id.bloodRequestView);
        requestQueue = Volley.newRequestQueue(getContext());
        //assign firebase variable
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        postBloodRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PostForBloodFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //get user data from firebase
        getUserData();
        getLastDonat();
        setBloodRequestData();
        return view;
    }

    private void getLastDonat() {
        userId = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(userId)
                .collection("activity").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getId().toString().equals("initial")){
                                    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    if (document.get("activity").toString().equals("Blood Donated")){
                                        String lastDonate= document.get("date").toString();
                                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                        try {
                                            Date date1 = myFormat.parse(lastDonate);
                                            Date date2 = myFormat.parse(currentDate);
                                            long diff = date2.getTime() - date1.getTime();
                                            bloodDonatTime.setText(String.valueOf((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                                            if ((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)<90){
                                                donateConfirmation.setText("You cont't donate blood yet");
                                                donateConfirmation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.main_color1, null));
                                            }
                                            else {
                                                donateConfirmation.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }else {
                                        donateConfirmation.setText("You dont't donate blood yet");
                                    }

                                }
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * patch blood request json data From API
     * useing Volley library
     */
    private void setBloodRequestData() {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject1.getJSONArray("Blood Request");
                            //set blood request data to recyclerview
                            setJsonData(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }

    /**
     * set jeson data in recyclerview
     * @param jsonArray
     * @throws JSONException
     */
    private void setJsonData(JSONArray jsonArray) throws JSONException {
        bloodRequestView.setLayoutManager(new LinearLayoutManager(getContext()));
        bloodReqiestViewModels = new ArrayList<>();
        int people=0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject hit = jsonArray.getJSONObject(i);
            Log.d("HOME_ACTIVITY", "onResponse: "+ blood);
            if (hit.getString("blood group").equals(blood)){
                people++;
            }
            String img = hit.getString("image");
            String nam = hit.getString("name");
            String bg = hit.getString("blood group");
            String loc = hit.getString("location");
            String hos = hit.getString("Hospital");
            String em = hit.getString("emergency");
            bloodReqiestViewModels.add(new BloodReqiestViewModel(img, nam, bg, loc, hos, em ));
        }
        Log.d("HOME_ACTIVITY", "onResponse:blood "+ people);
        //set total blood group match number
        bloodSearchingAmount.setText(String.valueOf(people));
        //attach recyclerview with adapter
        bloodRequestView.setAdapter(new BloodRequestViewAdapter(bloodReqiestViewModels, getContext()));
    }

    /**
     * get user blood group and last blood donate time
     */
    private void getUserData() {
        userId = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                String address="";
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    blood = document.get("bloodGroup").toString();
                    myBloodGroup.setText(blood);
                }
            }
        });

    }

    public void OnBtnClick(View view) {
        final int id = view.getId();
        switch (id){
            case R.id.btnPostNow:

                break;
        }
    }
}