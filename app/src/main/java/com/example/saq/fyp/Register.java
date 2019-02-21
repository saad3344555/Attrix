package com.example.saq.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity implements FirebaseHelper.RegisterCallBack {

    private TextView tv_register;
    private EditText name, email, mobile, pass, confirm_pass, uniName;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        pass = findViewById(R.id.pass);
        confirm_pass = findViewById(R.id.confirm_pass);
        uniName = findViewById(R.id.uniName);

        firebaseHelper = new FirebaseHelper(Register.this);

        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    if (passMathes()) {
                        firebaseHelper.setRegisterCallBack(Register.this);
                        firebaseHelper.registerTeacher(getRegisterModel());
                    }

                } else
                    Toast.makeText(Register.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean passMathes() {
        return pass.getText().toString().equals(confirm_pass.getText().toString());
    }

    private boolean validateFields() {
        if (name.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || confirm_pass.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() || mobile.getText().toString().isEmpty() || uniName.getText().toString().isEmpty())
            return false;
        else {
            return true;
        }
    }


    private SignInUpModel getRegisterModel() {
        SignInUpModel model = new SignInUpModel();

        model.setEmail(email.getText().toString().trim());
        model.setName(name.getText().toString().trim());
        model.setPass(pass.getText().toString().trim());
        model.setMobile(mobile.getText().toString().trim());
        model.setUniName(uniName.getText().toString().trim());

        return model;
    }

    @Override
    public void onRegister(boolean success, String id) {
        if (success) {
            AppGenericClass.getInstance(this).setPrefs(AppGenericClass.TOKEN, id);
            startActivity(new Intent(Register.this, Home.class));
            finish();
        } else
            Toast.makeText(Register.this, "User already exist", Toast.LENGTH_SHORT).show();
    }
}
