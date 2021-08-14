package com.example.xdonor;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PostForBloodFragment extends Fragment {

    Spinner bloodGroupSpinner;
    Button btnPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_for_blood, container, false);
        bloodGroupSpinner = view.findViewById(R.id.blood_group_spinner);
        btnPost = view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                Toast.makeText(getContext(), "Request Posted", Toast.LENGTH_SHORT).show();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                new HomeFragment()).addToBackStack(null).commit();
            }
        });
        setWidgets();
        return view;
    }

    private void setWidgets() {
        String[] bloodGroupList = {"A (Positive)", "O (Positive)", "B (Positive)", "AB (Positive)",
                "A (Negative)", "O (Negative)", "B (Negative)", "AB (Negative)"};
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, bloodGroupList);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView1 = (TextView) view;
                if (textView1 != null)
                    textView1.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}