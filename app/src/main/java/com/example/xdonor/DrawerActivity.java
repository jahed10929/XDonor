package com.example.xdonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class DrawerActivity extends AppCompatActivity {
    public NavigationView navigationView;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle drawerToggle;
    protected FrameLayout frameLayout;
    RelativeLayout lytProfile;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_activity);
        frameLayout = findViewById(R.id.content_frame);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);
        lytProfile = header.findViewById(R.id.head_lyt);
        //call navigation drawer
        setupNavigationDrawer();
    }

    /**
     * Setup navigationDrawer
     */
    private void setupNavigationDrawer() {
        Menu nav_Menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                drawer.closeDrawers();
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.drawer_dashboard:
                        fragment = new HomeFragment();
                        break;

                    case R.id.drawer_history:
                        fragment = new HIstoryFragment();
                        break;
                    case R.id.drawer_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                if (fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
}