package com.mp012202200038_01.apppuma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the input values
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Check if any field is empty
                if (name.isEmpty()) {
                    signupName.setError("Name cannot be empty!");
                } else if(email.isEmpty()) {
                    signupEmail.setError("Email cannot be empty!");
                } else if(username.isEmpty()) {
                    signupUsername.setError("Username cannot be empty!");
                } else if(password.isEmpty()) {
                    signupPassword.setError("Password cannot be empty!");
                } else {
                    // Check if the username already exists in the database
                    reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                signupUsername.setError("Username is already existed!");
                            } else {
                                // Username doesn't exist, proceed to check email
                                reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            signupEmail.setError("Email is already existed!");
                                        } else {
                                            // Email doesn't exist, proceed with signup
                                            HelperClass helperClass = new HelperClass(name, email, username, password);
                                            reference.child(username).setValue(helperClass);

                                            Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle any errors during the database query
                                        Toast.makeText(SignupActivity.this, "Error checking email existence", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any errors during the database query
                            Toast.makeText(SignupActivity.this, "Error checking username existence", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}