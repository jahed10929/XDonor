package com.example.xdonor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceTakenFragment extends Fragment {
    //Service taken api
    private String url = "https://jsonkeeper.com/b/7UMH";
    private RecyclerView donationView;
    ArrayList<ServiceTakenModel> donationViewModels;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_service_taken, container, false);
        donationView = view.findViewById(R.id.donationView);
        requestQueue = Volley.newRequestQueue(getContext());
        setLogData();

        return view;
    }
    private void setLogData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject1.getJSONArray("service taken");
                            //set blood request data to recyclerview
                            setJsonData(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        donationView.setLayoutManager(new LinearLayoutManager(getContext()));
        donationViewModels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject hit = jsonArray.getJSONObject(i);
            String yer = hit.getString("year");
            String dt = hit.getString("date");
            String tim = hit.getString("time");
            String loc = hit.getString("location");
            String hos = hit.getString("Hospital");
            String bg = hit.getString("bloodgroup");
            String lastD = hit.getString("lastDonation");
            donationViewModels.add(new ServiceTakenModel(yer, dt, tim, loc, hos, bg, lastD));
        }
        //attach recyclerview with adapter
        donationView.setAdapter(new ServiceTakenAdepter(donationViewModels));
    }
}