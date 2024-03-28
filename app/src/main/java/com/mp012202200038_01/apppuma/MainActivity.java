//MainActivity

package com.mp012202200038_01.apppuma;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> dataList;
    private ListEventAdapter adapter;

    ImageView onCardImageClick, profile;
    ImageView newEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String name, email, username, password;

        FloatingActionButton add = findViewById(R.id.new_event_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_note_dialog, null);
                TextInputLayout titleLayout, contentLayout, dayLayout, monthLayout, descLayout, timeLayout;
                titleLayout = view1.findViewById(R.id.titleLayout);
                contentLayout = view1.findViewById(R.id.contentLayout);
                dayLayout = view1.findViewById(R.id.dayLayout);
                monthLayout = view1.findViewById(R.id.monthLayout);
                descLayout = view1.findViewById(R.id.descLayout);
                timeLayout = view1.findViewById(R.id.timeLayout);

                TextInputEditText titleET, contentET, dayET, monthET, descET, timeET;
                titleET = view1.findViewById(R.id.titleET);
                contentET = view1.findViewById(R.id.contentET);
                dayET = view1.findViewById(R.id.dayET);
                monthET = view1.findViewById(R.id.monthET);
                descET = view1.findViewById(R.id.descET);
                timeET = view1.findViewById(R.id.timeET);
//until this
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add")
                        .setView(view1)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                    titleLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                    contentLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(dayET.getText()).toString().isEmpty()) {
                                    dayLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(monthET.getText()).toString().isEmpty()) {
                                    monthLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(descET.getText()).toString().isEmpty()) {
                                    descLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(timeET.getText()).toString().isEmpty()) {
                                    timeLayout.setError("This field is required!");
                                } else {
                                    ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    ListEvent note = new ListEvent();
                                    note.setTitle(titleET.getText().toString());
                                    note.setContent(contentET.getText().toString());
                                    note.setDay(dayET.getText().toString());
                                    note.setMonth(monthET.getText().toString());
                                    note.setDesc(descET.getText().toString());
                                    note.setTime(timeET.getText().toString());
                                    database.getReference().child("notes").push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(MainActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(MainActivity.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ListEvent> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ListEvent note = dataSnapshot.getValue(ListEvent.class);
                    Objects.requireNonNull(note).setKey(dataSnapshot.getKey());
                    arrayList.add(note);
                }

                // Iterate through the arrayList and print values
                for (ListEvent event : arrayList) {
                    System.out.println("Key: " + event.getKey());
                    System.out.println("Title: " + event.getTitle());
                    // Print other fields as needed
                }
                recyclerView.setVisibility(View.VISIBLE);

                // Create and set adapter
                ListEventAdapter adapter = new ListEventAdapter(MainActivity.this, arrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ListEventAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(ListEvent note) {
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_note_dialog, null);
                        TextInputLayout titleLayout, contentLayout, dayLayout, monthLayout, descLayout, timeLayout;
                        TextInputEditText titleET, contentET, dayET, monthET, descET, timeET;

                        titleET = view.findViewById(R.id.titleET);
                        contentET = view.findViewById(R.id.contentET);
                        dayET = view.findViewById(R.id.dayET);
                        monthET = view.findViewById(R.id.monthET);
                        descET = view.findViewById(R.id.descET);
                        timeET = view.findViewById(R.id.timeET);

                        titleLayout = view.findViewById(R.id.titleLayout);
                        contentLayout = view.findViewById(R.id.contentLayout);
                        dayLayout = view.findViewById(R.id.dayLayout);
                        monthLayout = view.findViewById(R.id.monthLayout);
                        descLayout = view.findViewById(R.id.descLayout);
                        timeLayout = view.findViewById(R.id.timeLayout);

                        titleET.setText(note.getTitle());
                        contentET.setText(note.getContent());
                        dayET.setText(note.getDay());
                        monthET.setText(note.getMonth());
                        descET.setText(note.getDesc());
                        timeET.setText(note.getTime());

                        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(titleET.getText()).toString().isEmpty()) {
                                            titleLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(contentET.getText()).toString().isEmpty()) {
                                            contentLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(dayET.getText()).toString().isEmpty()) {
                                            dayLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(monthET.getText()).toString().isEmpty()) {
                                            monthLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(descET.getText()).toString().isEmpty()) {
                                            descLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(timeET.getText()).toString().isEmpty()) {
                                            timeLayout.setError("This field is required!");
                                        } else {
                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            ListEvent note1 = new ListEvent();
                                            note1.setTitle(titleET.getText().toString());
                                            note1.setContent(contentET.getText().toString());
                                            note1.setDay(dayET.getText().toString());
                                            note1.setMonth(monthET.getText().toString());
                                            note1.setDesc(descET.getText().toString());
                                            note1.setTime(timeET.getText().toString());
                                            database.getReference().child("notes").child(note.getKey()).setValue(note1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(MainActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("Deleting...");
                                        progressDialog.show();
                                        database.getReference().child("notes").child(note.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event here (you can leave it empty if not needed)
            }

        });

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        profile = findViewById(R.id.imgProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }
}