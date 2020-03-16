package com.wakili.smarttailor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.wakili.smarttailor.Fragments.CompletedTasksFragment;
import com.wakili.smarttailor.Fragments.Customers;
import com.wakili.smarttailor.Fragments.GalleryFragment;
import com.wakili.smarttailor.Fragments.MesurementFragment;
import com.wakili.smarttailor.Fragments.PendingTasksFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;


    private FirebaseUser user;
    private AppBarConfiguration mAppBarConfiguration;
    Toolbar toolbar;
    private final int REQUEST_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        if (user == null) {
            // not logged in, launch the Log In activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if (user != null) {

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initializePermissions();




        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                drawer.closeDrawers();
                Intent intent;
                switch (id){

                    case R.id.nav_styles:
                        GalleryFragment galleryFragment  = new GalleryFragment();
                        getSupportFragmentManager().beginTransaction()
                                //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                                .replace(R.id.nav_host_fragment,galleryFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_customer:
                        toolbar.setTitle("MY CUSTOMERS");
                        Customers customers = new Customers();
                        getSupportFragmentManager().beginTransaction()
                                //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                                .replace(R.id.nav_host_fragment,customers)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_order:
                        toolbar.setTitle("MY CUSTOMERS");
                        PendingTasksFragment pendingTasksFragment = new PendingTasksFragment();
                        getSupportFragmentManager().beginTransaction()
                                //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                                .replace(R.id.nav_host_fragment,pendingTasksFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_finished:
                        toolbar.setTitle("MY PURCHASES");
                        CompletedTasksFragment completedTasksFragment = new CompletedTasksFragment();
                        getSupportFragmentManager().beginTransaction()
                                //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                                .replace(R.id.nav_host_fragment, completedTasksFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_addcustomer:
                        toolbar.setTitle("MY SALES");
                        MesurementFragment mesurementFragment = new MesurementFragment();
                        getSupportFragmentManager().beginTransaction()
                                //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                                .replace(R.id.nav_host_fragment, mesurementFragment)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.nav_exit:
                        System.exit(0);
                        finish();
                        break;

                }
                return false;
            }
        });
    }
    private void initializePermissions(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        //checking for storage permission to write images for upload
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, perms, REQUEST_PERMISSIONS);

            //checking for WRITE_EXTERNAL_STORAGE permission
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            //checking for CAMERA permissions
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
