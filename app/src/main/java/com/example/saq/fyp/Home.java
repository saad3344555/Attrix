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

public class Home extends AppCompatActivity implements ClassRyclerAdapter.CameraInterface {
    //home
    private List<ClassModel> Classes = new ArrayList<>();
    private RecyclerView ClassRecyclerView;
    private ClassRyclerAdapter ClassAdapter;
    private static final int RC_CAMERA = 420;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        ClassRecyclerView = (RecyclerView) findViewById(R.id.classrecylerv);
        ClassAdapter = new ClassRyclerAdapter(Classes, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ClassRecyclerView.setLayoutManager(mLayoutManager);
        ClassRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ClassRecyclerView.setAdapter(ClassAdapter);


        PrepareClasses();


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ClassRegisteration.class));
            }
        });
    }

    private void PrepareClasses() {
        this.Classes.add(new ClassModel("606", R.drawable.google, "Physics II", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));
        this.Classes.add(new ClassModel("603", R.drawable.facebook, "ICS II", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));
        this.Classes.add(new ClassModel("600", R.drawable.google, "Stats II", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));
        this.Classes.add(new ClassModel("604", R.drawable.facebook, "Calculas II", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));
        this.Classes.add(new ClassModel("504", R.drawable.google, "English II", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));
        this.Classes.add(new ClassModel("304", R.drawable.facebook, "Urdu", "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text."));

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
        }
    }
}
