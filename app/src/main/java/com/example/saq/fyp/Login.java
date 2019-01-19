package com.example.saq.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements FirebaseHelper.SignInCallBack {
    private Button bt_login;
    private EditText et_pass,et_mobileNo;
    private TextView register;
    private FirebaseHelper firebaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_mobileNo = findViewById(R.id.et_mobileNo);
        et_pass= findViewById(R.id.et_pass);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper = new FirebaseHelper(Login.this);
                firebaseHelper.setSignInCallBack(Login.this);
                firebaseHelper.signInTeacher(et_mobileNo.getText().toString().trim(), et_pass.getText().toString().trim());            }
        });

    }

    @Override
    public void onSignIn(int code,String id) {
        if (code == 200) {
            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.TOKEN,id);
            startActivity(new Intent(Login.this, Home.class));
        }
        else if (code == 201)
            Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Login.this, "Mobile No or Password is incorrect", Toast.LENGTH_SHORT).show();
    }
}
