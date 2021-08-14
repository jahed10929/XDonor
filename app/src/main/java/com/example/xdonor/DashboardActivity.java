package com.example.xdonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DashboardActivity extends DrawerActivity {
    private static final String TAG = "MAIN_ACTIVITY";
    public static final String DESHBOARD_TAG = "DESHBOARD_FRAGMENT_TAG";
    public static final String HISTORY_TAG = "HISTORY_FRAGMENT_TAG";
    public static final String PROFILE_TAG = "PROFILE_FRAGMENT_TAG";
    public static final String MAIN_ACTIVITY_FRAGMENT = "HOME_ACTIVITY_FRAGMENTS";
    ChipNavigationBar navigationBar;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        navigationBar = findViewById(R.id.navigation_view);
        if (savedInstanceState==null){
            navigationBar.setItemSelected(R.id.dashboard, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.dashboard:
                        fragment = new HomeFragment();
                        break;
                    case R.id.history:
                        fragment = new HIstoryFragment();
                        break;
                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
                if (fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

                }
            }
        });
        //todo create nav drower
        drawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        drawer, toolbar,
                        R.string.drawer_open,
                        R.string.drawer_close
                ) {};
    }

}