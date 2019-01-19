package com.example.saq.fyp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements ClassRyclerAdapter.CameraInterface,FirebaseHelper.GetClassCallback {
    //home
    private List<ClassModel> Classes = new ArrayList<>();
    private RecyclerView ClassRecyclerView;
    private ClassRyclerAdapter ClassAdapter;
    private static final int RC_CAMERA = 420;
    FirebaseHelper firebaseHelper;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);


        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setGetClassCallback(this);
        firebaseHelper.getClasses(AppGenericClass.getInstance(this).getPrefs(AppGenericClass.TOKEN));

        ClassRecyclerView = (RecyclerView) findViewById(R.id.classrecylerv);
        ClassAdapter = new ClassRyclerAdapter(Classes, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ClassRecyclerView.setLayoutManager(mLayoutManager);
        ClassRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ClassRecyclerView.setAdapter(ClassAdapter);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Home.this, ClassRegisteration.class),999);
            }
        });
    }

    @Override
    public void openChooser() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, RC_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bmp.recycle();
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent in = new Intent(Home.this, MarkAttendance.class);
                    in.putExtra("AttedanceImageBytes", byteArray);
                    startActivity(in);
                }
                break;

            case 999:
                if(resultCode == 200){
                    firebaseHelper.getClasses(AppGenericClass.getInstance(Home.this).getPrefs(AppGenericClass.TOKEN));
                }
                break;
        }
    }

    @Override
    public void getClasses(List<ClassModel> classModel) {
        Classes = classModel;
        ClassRecyclerView.setAdapter(new ClassRyclerAdapter(Classes,Home.this,Home.this));
    }
}
