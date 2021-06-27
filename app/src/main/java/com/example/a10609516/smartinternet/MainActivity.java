package com.example.a10609516.smartinternet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    private TextView acc_txt;

    private String LOG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //動態取得 View 物件
        InItFunction();
        //按壓事件監聽器
        OnClickListener();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_device, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * 動態取得 View 物件
     */
    private void InItFunction() {
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        LayoutInflater inflater = getLayoutInflater();
        View myView = inflater.inflate(R.layout.nav_header_main, null);
        //TextView myTextView = (TextView) myView.findViewById(R.id.tvDerham);
        //View inflatedView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        acc_txt = (TextView) myView.findViewById(R.id.acc_txt);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences user_email = getSharedPreferences("user_email", MODE_PRIVATE);
                String U_ACC = user_email.getString("ID", "");
                Log.e(LOG, U_ACC);
                acc_txt.setText(U_ACC);
            }
        });


    }

    /**
     * 按壓事件監聽器
     */
    private void OnClickListener() {
        // 為navigatin_view設置點擊事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // 點選時收起選單
                drawer.closeDrawer(GravityCompat.START);

                // 取得選項id
                int id = item.getItemId();

                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.nav_device) {
                    // 按下「首頁」要做的事
                    Intent device_intent = new Intent(MainActivity.this, DeviceActivity.class);
                    startActivity(device_intent);
                    return true;
                }
                /*else if (id == R.id.miaoli) {
                    // 按下「使用說明」要做的事

                    return true;
                }*/

                return false;
            }
        });
    }
}
