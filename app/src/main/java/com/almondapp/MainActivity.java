package com.almondapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.almondapp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

Button btnSingIn, btnRegister;
FirebaseAuth auth;
FirebaseDatabase db;
DatabaseReference users;
ConstraintLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSingIn = findViewById(R.id.buttonSignIn);
        btnRegister = findViewById(R.id.buttonRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        root = findViewById(R.id.root_layot);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Авторизация");
        dialog.setMessage("Введите все данные для авторизации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View signInWindow = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(signInWindow);

        final MaterialEditText email = signInWindow.findViewById(R.id.emailField);
        final MaterialEditText pass = signInWindow.findViewById(R.id.passwordField);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString()))
                {
                    Snackbar.make(root, "Введите вашу почту!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(pass.getText().toString().length()<6)
                {
                    Snackbar.make(root, "Введите пароль более 6 символов!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                //Авторизуем пользователя
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Snackbar.make(root, "Добро пожаловать в чат компании Almond", Snackbar.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);
        dialog.setView(registerWindow);

        final MaterialEditText email = registerWindow.findViewById(R.id.emailField);
        final MaterialEditText firstName = registerWindow.findViewById(R.id.firstNameField);
        final MaterialEditText lastName = registerWindow.findViewById(R.id.lastNameField);
        final MaterialEditText phone = registerWindow.findViewById(R.id.phoneField);
        final MaterialEditText pass = registerWindow.findViewById(R.id.passwordField);

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString()))
                {
                    Snackbar.make(root, "Введите вашу почту!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(firstName.getText().toString()))
                {
                    Snackbar.make(root, "Введите ваше Имя!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(lastName.getText().toString()))
                {
                    Snackbar.make(root, "Введите вашу Фамилию!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(phone.getText().toString()))
                {
                    Snackbar.make(root, "Введите ваш телефон!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if(pass.getText().toString().length()<6)
                {
                    Snackbar.make(root, "Введите пароль более 6 символов!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                //Регаем пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setFirstName(firstName.getText().toString());
                                user.setLastName(lastName.getText().toString());
                                user.setPhone(phone.getText().toString());
                                user.setPass(pass.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Snackbar.make(root, "Ошибка регистрации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }
}
