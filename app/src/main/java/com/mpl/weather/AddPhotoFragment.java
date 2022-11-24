package com.mpl.weather;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhotoFragment extends BottomSheetDialogFragment {

    Button uploadBtn;
    TextView dateText;
    TextView temperatureText;

    public String temperature;
    public String state;

    Fragment fragment;
    Context context;

    public AddPhotoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);

        uploadBtn = (Button) view.findViewById(R.id.uploadBtn);
        dateText = (TextView) view.findViewById(R.id.dateText);
        temperatureText = (TextView) view.findViewById(R.id.temperatureText);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String baseDate = dateFormat.format(date);

        dateText.setText(baseDate);
        setTemperatureText();
        //setStateBackGround(state);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = getFragmentManager().findFragmentById(R.id.mainLayout);
                //((MainActivity)getActivity()).closeFragment(fragment);
                dismiss();
            }
        });

        return view;
    }

    public void setTemperatureText() {
        temperatureText.setText(temperature + "도");
    }
}