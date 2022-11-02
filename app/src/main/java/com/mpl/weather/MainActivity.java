package com.mpl.weather;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.weather.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ConstraintLayout weatherLayout;
    TextView stateTextview;
    TextView tempTextView;
    ImageView weatherIcon;
    CalendarView calenderView;
    TextView date;

    private String weatherState;
    private String weatherTemp;
    private WeatherData weatherData;

    FloatingActionButton fab, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    boolean isOpen = false;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherData = new WeatherData();
        weatherLayout = (ConstraintLayout) findViewById(R.id.weatherLayout);
        stateTextview = (TextView) findViewById(R.id.weatherState);
        tempTextView = (TextView) findViewById(R.id.temperature);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        new Thread() {
            public void run() {
                String[] temp = new String[2];
                try {
                    temp = getWeatherData(weatherData);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                weatherState = temp[0];
                weatherTemp = temp[1];
                Bundle bundle = new Bundle();
                bundle.putString("State", weatherState);

                Message msg = handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();

        ImageSlider imageSlider = findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://i.pinimg.com/originals/21/42/0b/21420b241e3d4bb084c9200ff50c947a.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/originals/29/54/e0/2954e0d566fa420c1a449c04b4123464.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/originals/fb/2e/c3/fb2ec3b24144c92a25ce186fbe509bf2.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/originals/e6/ae/44/e6ae4467903e271f31f4e0d64ce29f69.jpg"));
        imageSlider.setImageList(slideModels, true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Toast.makeText(MainActivity.this, "gallery upload", Toast.LENGTH_SHORT).show();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Toast.makeText(MainActivity.this, "take a photo", Toast.LENGTH_SHORT).show();
            }
        });

        calenderView = findViewById(R.id.calendarView);
        date = findViewById(R.id.date);

        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String todayDate = (month+1) + "/" + dayOfMonth + "/" + year;
                Log.d("date", todayDate);
                date.setText(todayDate);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new calendarFragment()).commit();
            navigationView.setCheckedItem(R.id.calendarFragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendarFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new calendarFragment()).commit();
                break;
            case R.id.galleryFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new galleryFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private String[] getWeatherData(WeatherData weatherData) throws JSONException, IOException {
        final String[] dataString = new String[2];
        String[] temp = weatherData.lookUpWeather();
        dataString[0] = temp[0];
        dataString[1] = temp[1];
        return dataString;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String msgString = bundle.getString("state");
            changeWeatherStateBackground(weatherState);
            tempTextView.setText(weatherTemp + "도");
        }
    };

    private void changeWeatherStateBackground(String state) {
        if (state.equals("맑음")) {
            weatherLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.sunny));
            stateTextview.setText(getApplicationContext().getResources().getString(R.string.sunny));
            weatherIcon.setImageResource(R.drawable.sunny);
        } else if (state.equals("흐림")) {
            weatherLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.dark));
            stateTextview.setText(getApplicationContext().getResources().getString(R.string.dark));
            weatherIcon.setImageResource(R.drawable.dark);
        } else if (state.equals("구름많음")) {
            weatherLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.cloudy));
            stateTextview.setText(getApplicationContext().getResources().getString(R.string.cloudy));
            weatherIcon.setImageResource(R.drawable.cloudy);
        } else {
            weatherLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.rain));
            stateTextview.setText(getApplicationContext().getResources().getString(R.string.rain));
            weatherIcon.setImageResource(R.drawable.rain);
        }
    }

    private void animateFab() {
        if(isOpen) {
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;
        }
        else {
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;
        }
    }
}