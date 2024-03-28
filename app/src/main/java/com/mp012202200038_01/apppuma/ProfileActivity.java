package com.mp012202200038_01.apppuma;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
public class ProfileActivity extends AppCompatActivity {
    private static final int EDIT_PROFILE_REQUEST = 1;
    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName, titleUsername;
    Button editProfile, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        logout = findViewById(R.id.logoutButton);
        showAllUserData();

        TextView profilePassword = findViewById(R.id.profilePassword);
        profilePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        logout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }));
    }
    public void showAllUserData(){
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        String emailUser = intent.getStringExtra("email");
        String usernameUser = intent.getStringExtra("username");
        String passwordUser = intent.getStringExtra("password");

        titleName.setText(nameUser);
        titleUsername.setText(usernameUser);
        profileName.setText(nameUser);
        profileEmail.setText(emailUser);
        profileUsername.setText(usernameUser);
        profilePassword.setText(passwordUser);
    }
    public void passUserData(){
        String userUsername = profileUsername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}