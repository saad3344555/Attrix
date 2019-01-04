package com.example.saq.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Login extends AppCompatActivity {
    private Button bt_login;
    private ImageView iv_createAccount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        iv_createAccount = findViewById(R.id.iv_createAccount);
        iv_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));

            }
        });
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Home.class));
            }
        });

    }
}
