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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarImage = (ImageView) view.findViewById(R.id.calendarImage);
        weatherTextView = (TextView) view.findViewById(R.id.weatherTextView);
        rateTextView = (TextView) view.findViewById(R.id.rateTextView);

        User userInstance = User.getInstance(uid, id);
        uid = userInstance.getUid();
        id = userInstance.getId();

        userDatabase = firebaseDatabase.getReference("Users").child(uid);

        DatabaseReference beforeFashion = userDatabase.child("fashion").child("date").child("2022-12-09");
        DatabaseReference afterFashion = userDatabase.child("fashion").child("date").child("2022-12-11");

        List<Cloth> clothList = new ArrayList<>();
        beforeFashion.setValue(new Fashion("2022-12-09", clothList, "0", "추움", "https://cdn.pixabay.com/photo/2022/12/01/14/46/city-7629244_1280.jpg"));
        afterFashion.setValue(new Fashion("2022-12-11", clothList, "0", "적당함", "https://cdn.pixabay.com/photo/2022/12/01/14/46/city-7629244_1280.jpg"));

        //userDatabase = firebaseDatabase.getReference("Users").child("OVUC3LwGHlNvMFbVsFz0fVHhheu1");


        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // on below line we are setting up our horizontal calendar view and passing id our calendar view to it.
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                // on below line we are adding a range
                // as start date and end date to our calendar.
                .range(startDate, endDate)
                // on below line we are providing a number of dates
                // which will be visible on the screen at a time.
                .datesNumberOnScreen(5)
                // at last we are calling a build method
                // to build our horizontal recycler view.
                .build();
        // on below line we are setting calendar listener to our calendar view.
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                // on below line we are printing date
                // in the logcat which is selected.
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
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        // Save your data in here
                        calendarImage.setImageURI(Uri.parse(fashion.photoURL));
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                    }
                    weatherTextView.setText(fashion.weather);
                    rateTextView.setText(fashion.rate);
                } else {
                    calendarImage.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                    weatherTextView.setText("입력된 패션 정보가 없습니다.");
                    rateTextView.setText("정보를 입력해주세요.");
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