package com.mp012202200038_01.apppuma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class EditProfileActivity extends AppCompatActivity {
    TextView editUsername;
    EditText editName, editEmail, editPassword;
    Button saveButton, backButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backtpButton);

        showData();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("name", nameUser);
                intent.putExtra("email", emailUser);
                intent.putExtra("username", usernameUser);
                intent.putExtra("password", passwordUser);
                startActivity(intent);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

    }
    private void saveChanges() {
        String newName = editName.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newPassword = editPassword.getText().toString();

        if (!nameUser.equals(newName) || !emailUser.equals(newEmail) || !passwordUser.equals(newPassword)) {
            reference.child(usernameUser).child("name").setValue(newName);
            reference.child(usernameUser).child("email").setValue(newEmail);
            reference.child(usernameUser).child("password").setValue(newPassword);

            Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();

            // Update local variables
            nameUser = newName;
            emailUser = newEmail;
            passwordUser = newPassword;
        } else {
            Toast.makeText(EditProfileActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
        }
    }
    public void showData(){
        Intent intent = getIntent();
        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
}