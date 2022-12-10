package com.mpl.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    Button signupBtn;
    EditText idTextView, emailTextView, passWordTextView, passwordCheckTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth =  FirebaseAuth.getInstance();
        signupBtn = (Button) findViewById(R.id.signupBtn);
        idTextView = (EditText) findViewById(R.id.idTextView);
        emailTextView = (EditText) findViewById(R.id.emailTextView);
        passWordTextView = (EditText) findViewById(R.id.passWordTextView);
        passwordCheckTextView = (EditText) findViewById(R.id.passwordCheckTextView);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = idTextView.getText().toString().trim();
                final String email = emailTextView.getText().toString().trim();
                String password = passWordTextView.getText().toString().trim();
                String passwordCheck = passwordCheckTextView.getText().toString().trim();

                if (password.equals(passwordCheck)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(id).build();
                                user.updateProfile(profileUpdates);
                                String name = user.getDisplayName();

                                HashMap<Object, String> hashMap = new HashMap<>();

                                hashMap.put("uid", uid);
                                hashMap.put("email", email);
                                hashMap.put("name", name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).child("userInfo").setValue(hashMap);
                                User userInstance = User.getInstance(uid, name);

                                initDatabase(reference.child(uid));

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignupActivity.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void initDatabase(DatabaseReference databaseReference) {
        HashMap<String, Cloth> clothHashMap = new HashMap();
        String[] upClothes = {"없음", "민소매", "반팔", "원피스", "셔츠", "긴팔", "가디건", "후드티", "맨투맨", "운동복", "유니폼", "정장", "니트"};
        String[] downClothes = {"없음", "반바지", "청바지", "면바지", "슬랙스", "조거팬츠", "스키니진", "레깅스", "스타킹", "운동복", "유니폼", "정장", "미니스커트", "스커트"};
        String[] outerClothes = {"없음", "자켓", "야상", "코트", "가죽자켓", "패딩", "플리스", "후드집업", "우비", "무스탕", "조끼"};
        String[] AccessaryClothes = {"없음", "귀걸이", "목걸이", "팔찌", "목도리", "장갑", "선글라스", "모자"};
        HashMap<String, Statistics> statisticHashMap = new HashMap();

        for (int i = 0; i < upClothes.length; i++) {
            String clothString = upClothes[i];
            Cloth cloth = new Cloth("up", clothString);
            clothHashMap.put(clothString + ", " + "up", cloth);

            for (int temp = -15; temp < 40; temp += 5) {
                databaseReference.child("Statistics").child(String.valueOf(temp)).child("up").child(clothString).setValue(new Statistics(clothString, 0));
            }
        }
        for (int i = 0; i < downClothes.length; i++) {
            String clothString = downClothes[i];
            Cloth cloth = new Cloth("down", clothString);
            clothHashMap.put(clothString + ", " + "down", cloth);
            for (int temp = -15; temp < 40; temp += 5) {
                databaseReference.child("Statistics").child(String.valueOf(temp)).child("down").child(clothString).setValue(new Statistics(clothString, 0));
            }
        }
        for (int i = 0; i < outerClothes.length; i++) {
            String clothString = outerClothes[i];
            Cloth cloth = new Cloth("outer", clothString);
            clothHashMap.put(clothString + ", " + "outer", cloth);
            for (int temp = -15; temp < 40; temp += 5) {
                databaseReference.child("Statistics").child(String.valueOf(temp)).child("outer").child(clothString).setValue(new Statistics(clothString, 0));
            }
        }
        for (int i = 0; i < AccessaryClothes.length; i++) {
            String clothString = AccessaryClothes[i];
            Cloth cloth = new Cloth("acc", clothString);
            clothHashMap.put(clothString + ", " + "acc", cloth);
            for (int temp = -15; temp < 40; temp += 5) {
                databaseReference.child("Statistics").child(String.valueOf(temp)).child("acc").child(clothString).setValue(new Statistics(clothString, 0));
            }
        }

        databaseReference.child("Clothes").setValue(clothHashMap);
        //databaseReference.child("Statistics").setValue(statisticHashMap);
    }
}