package com.almondapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {
ImageButton imgBtnBack;
DatabaseReference emailDb, fDb, lDb, pDb, passDb;
FirebaseAuth mAuth;
String userId;
FirebaseUser user;
EditText emailEd, firstNameEd, lastNameEd, phoneEd, passEd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        emailEd = findViewById(R.id.emailEdit);
        firstNameEd = findViewById(R.id.firstNameEdit);
        lastNameEd = findViewById(R.id.lastNameEdit);
        phoneEd = findViewById(R.id.phoneEdit);
        passEd = findViewById(R.id.passwordEdit);
        imgBtnBack = findViewById(R.id.imageButtonBack);
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            }
        });
        emailDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("email");
        fDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("firstName");
        lDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("lastName");
        pDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("phone");
        passDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pass");
        emailDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                emailEd.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        fDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                firstNameEd.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        lDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lastNameEd.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        pDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                phoneEd.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        passDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                passEd.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}