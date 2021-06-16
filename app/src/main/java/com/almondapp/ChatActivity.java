package com.almondapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class ChatActivity extends AppCompatActivity {
    public static int SIGN_IN_REQUEST_CODE = 1;
    ImageButton imgBtnSnd, imgBtnBack;
    EditText usrInput;
    RecyclerView chtWindow;
    MessageController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imgBtnBack = findViewById(R.id.imageButtonBack);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            Toast.makeText(this,
                    "Добро пожаловать, " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }
        imgBtnSnd = (ImageButton) findViewById(R.id.imageButtonSend);
        usrInput = (EditText) findViewById(R.id.userInput);
        chtWindow = (RecyclerView) findViewById(R.id.chatWindow);

        controller = new MessageController();
        controller.setIncomingLayout(R.layout.message_item);
        controller.setOutgoingLayout(R.layout.message_item);
        controller.setMessageTextId(R.id.messageText);
        controller.setUserNameId(R.id.userName);
        controller.setMessageTimeId(R.id.messageDate);
        controller.appendTo(chtWindow, this);

        controller.addMessage(
                new MessageController.Message("Добрый вечер колллеги! Жду ваших отчетов о выполненной работе.", "antoncharyshin44@mail.ru", true)
        );
        controller.addMessage(
                new MessageController.Message("Добрый вечер! Выполнил тестирование БД и выслал вам отчет на почту.", "andreylojnikov112@gmail.ru", false)
        );
        imgBtnSnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = usrInput.getText().toString();
                controller.addMessage(new MessageController.Message(text, FirebaseAuth.getInstance().getCurrentUser().getEmail(), false));
                usrInput.setText("");
            }
        });
    }
    private void displayChatMessages() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }
}