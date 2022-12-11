package com.mpl.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    ImageView calendarImage;
    TextView weatherTextView, rateTextView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userDatabase;
    String uid = null;
    String id = null;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarImage = (ImageView) view.findViewById(R.id.calendarImage);
        weatherTextView = (TextView) view.findViewById(R.id.weatherTextView);
        rateTextView = (TextView) view.findViewById(R.id.rateTextView);

        User userInstance = User.getInstance(uid, id);
        uid = userInstance.getUid();
        id = userInstance.getId();

        userDatabase = firebaseDatabase.getReference("Users").child(uid);

        DatabaseReference before1Fashion = userDatabase.child("fashion").child("date").child("2022-12-10");
        DatabaseReference before2Fashion = userDatabase.child("fashion").child("date").child("2022-12-09");

        List<Cloth> clothList = new ArrayList<>();
        before1Fashion.setValue(new Fashion("2022-12-10", clothList, "3.0", "적당함", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbF16Sq%2FbtrThfLkLVY%2FckagLjw1DkCxJBW8ACJJM0%2Fimg.jpg"));
        before2Fashion.setValue(new Fashion("2022-12-09", clothList, "-2.0", "추움", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F4CTxf%2FbtrThkTaZgC%2F62dz3wqwqqRcpY1U8HNka1%2Fimg.jpg"));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate).datesNumberOnScreen(5).build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Log.e("TAG", "CURRENT DATE IS " + date.getTime());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String baseDate = dateFormat.format(date.getTime());

                Log.e("TAG", "CURRENT DATE IS " + baseDate);

                DatabaseReference fashion = userDatabase.child("fashion").child("date").child(baseDate);

                changeView(fashion);
            }
        });

        return view;
    }

    private void changeView(DatabaseReference dataReference) {
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Fashion fashion = dataSnapshot.getValue(Fashion.class);

                    Log.e("TAG", "CURRENT DATE IS " + fashion.toString());
                    if (fashion.photoURL.startsWith("http")) {
                        Glide.with(getActivity())
                                .load(fashion.photoURL)
                                .into(calendarImage);
                    } else {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            calendarImage.setImageURI(Uri.parse(fashion.photoURL));
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        }
                        weatherTextView.setText(fashion.weather);
                        rateTextView.setText(fashion.rate);
                    }

                } else {
                    calendarImage.setImageResource(R.drawable.add);
                    weatherTextView.setText("입력된 패션 정보가 없습니다.");
                    rateTextView.setText("정보를 입력해주세요.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("failed change tags", "Failed to read value.", error.toException());
            }
        });
    }
}