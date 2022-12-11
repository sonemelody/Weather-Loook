package com.mpl.weather;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RelativeLayout weatherLayout;
    TextView stateTextview;
    TextView tempTextView;
    ImageView weatherIcon;

    private String weatherState;
    private String weatherTemp;
    private WeatherData weatherData;

    FloatingActionButton fab, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;

    boolean isOpen = false;

    private FragmentManager fragmentManager;
    private AddPhotoFragment addPhotoFragment;

    private Chip tagOuter, tagTop, tagBottom, tagAcc;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userDatabase;

    private TextView userName;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try{
            weatherData = new WeatherData();
            weatherLayout = (RelativeLayout) view.findViewById(R.id.weatherLayout);
            stateTextview = (TextView) view.findViewById(R.id.weatherState);
            tempTextView = (TextView) view.findViewById(R.id.temperature);
            weatherIcon = (ImageView) view.findViewById(R.id.weatherIcon);

            tagOuter = (Chip) view.findViewById(R.id.tagOuter);
            tagTop = (Chip) view.findViewById(R.id.tagTop);
            tagBottom = (Chip) view.findViewById(R.id.tagBottom);
            tagAcc = (Chip) view.findViewById(R.id.tagAcc);
        } catch (NullPointerException e) {

        }

        fragmentManager = getParentFragmentManager();

        addPhotoFragment = new AddPhotoFragment(getActivity());

        String uid = null;
        String id = null;
        User userInstance = User.getInstance(uid, id);
        uid = userInstance.getUid();
        id = userInstance.getId();

        ImageSlider imageSlider = view.findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/24/b4/f1/24b4f10ed87ca9ce15d8fd0a99da4d54.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/2b/0f/29/2b0f291b78281ffe20c720bd1a100f24.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/92/a8/fd/92a8fdf46e086529b8e9c78227c60891.jpg"));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/90/d3/6a/90d36af2b63b08596a923ce5792b0838.jpg"));
        imageSlider.setImageList(slideModels, true);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fabOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhotoFragment.show(getParentFragmentManager(), addPhotoFragment.getTag());
                animateFab();
            }
        });

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

        return view;
    }

    private String[] getWeatherData(WeatherData weatherData) throws JSONException, IOException {
        final String[] dataString = new String[2];
        String[] temp = weatherData.lookUpWeather();
        dataString[0] = temp[0];
        dataString[1] = temp[1];
        return dataString;
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String msgString = bundle.getString("state");
            changeWeatherStateBackground(weatherState);
            float temp = Float.parseFloat(weatherTemp);
            changeTags(temp);
            tempTextView.setText(weatherTemp + "도");
            addPhotoFragment.temperature = weatherTemp;
            addPhotoFragment.state = weatherState;
        }
    };

    private void changeWeatherStateBackground(String state) {
        if ("맑음".equals(state)) {
            weatherLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.sunny));
            stateTextview.setText(getActivity().getResources().getString(R.string.sunny));
            weatherIcon.setImageResource(R.drawable.sunny);
        } else if ("흐림".equals(state)) {
            weatherLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.dark));
            stateTextview.setText(getActivity().getResources().getString(R.string.dark));
            weatherIcon.setImageResource(R.drawable.dark);
        } else if ("구름많음".equals(state)) {
            weatherLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.cloudy));
            stateTextview.setText(getActivity().getResources().getString(R.string.cloudy));
            weatherIcon.setImageResource(R.drawable.cloudy);
        } else {
            weatherLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.rain));
            stateTextview.setText(getActivity().getResources().getString(R.string.rain));
            weatherIcon.setImageResource(R.drawable.rain);
        }
    }

    private void animateFab() {
        if(isOpen) {
            fab.startAnimation(rotateForward);
            isOpen=false;
        }
        else {
            fab.startAnimation(rotateBackward);
            isOpen=true;
        }
    }

    private void changeTags(float temperature) {
        String uid = null;
        String id = null;
        User userInstance = User.getInstance(uid, id);
        uid = userInstance.getUid();
        id = userInstance.getId();

        int temp = (int) ((int) (temperature / 5) * 5);

        userDatabase = firebaseDatabase.getReference("Users").child(uid);
        DatabaseReference statistics = userDatabase.child("Statistics").child(String.valueOf(temp));
        DatabaseReference outers = statistics.child("outer");
        DatabaseReference ups = statistics.child("up");
        DatabaseReference downs = statistics.child("down");
        DatabaseReference accs = statistics.child("acc");

        String[] downClothes = {"없음", "반바지", "청바지", "면바지", "슬랙스", "조거팬츠", "스키니진", "레깅스", "스타킹", "운동복", "유니폼", "정장", "미니스커트", "스커트"};
        String[] outerClothes = {"없음", "자켓", "야상", "코트", "가죽자켓", "패딩", "플리스", "후드집업", "우비", "무스탕", "조끼"};
        String[] accessaryClothes = {"없음", "귀걸이", "목걸이", "팔찌", "목도리", "장갑", "선글라스", "모자"};
        String[] upClothes = {"없음", "민소매", "반팔", "원피스", "셔츠", "긴팔", "가디건", "후드티", "맨투맨", "운동복", "유니폼", "정장", "니트"};

        changeTag(ups, upClothes, tagTop);
        changeTag(downs, downClothes, tagBottom);
        changeTag(outers, outerClothes, tagOuter);
        changeTag(accs, accessaryClothes, tagAcc);
    }

    private void changeTag(DatabaseReference statisticsByCategory, String[] clothes, Chip tag) {
        statisticsByCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                String string = "";

                for (int i = 0; i < clothes.length; i++) {
                    int currentCount = dataSnapshot.child(clothes[i]).child("wearCount").getValue(Integer.class);

                    if (currentCount > count) {
                        count = currentCount;
                        string = clothes[i];
                    }
                    tag.setText(string);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("failed change tags", "Failed to read value.", error.toException());
            }
        });
    }
}