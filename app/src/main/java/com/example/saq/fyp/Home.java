package com.example.saq.fyp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.saq.fyp.Interfaces.ClassSelectedListener;
import com.example.saq.fyp.common.Common;
import com.example.saq.fyp.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Home extends AppCompatActivity implements ClassRyclerAdapter.CameraInterface, FirebaseHelper.GetClassCallback, ClassSelectedListener {
    //home
    private List<ClassModel> Classes = new ArrayList<>();
    private RecyclerView ClassRecyclerView;
    private ClassRyclerAdapter ClassAdapter;
    private static final int RC_CAMERA = 420;
    private static final int RC_GALLERY = 421;
    FirebaseHelper firebaseHelper;
    FloatingActionButton floatingActionButton;
    public static ClassModel SELECTED_CLASS = null;
    FloatingActionButton logout;
    public static Bitmap ATTENDACE_IMAGE = null;
    List<Student> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);
        logout = findViewById(R.id.logout);

        getStudents();
        AppGenericClass.getInstance(this).setPrefs(AppGenericClass.ONLINE, "true");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppGenericClass.getInstance(Home.this).clearPrefs(Home.this);
                Intent in = new Intent(Home.this, Login.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });


        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setGetClassCallback(this);
        firebaseHelper.getClasses(AppGenericClass.getInstance(this).getPrefs(AppGenericClass.TOKEN));

        ClassRecyclerView = (RecyclerView) findViewById(R.id.classrecylerv);
        ClassAdapter = new ClassRyclerAdapter(Classes, this, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ClassRecyclerView.setLayoutManager(mLayoutManager);
        ClassRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ClassRecyclerView.setAdapter(ClassAdapter);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Home.this, ClassRegisteration.class), 999);
            }
        });
    }

    private void getStudents() {
        list = new ArrayList<>();
        Paper.book().delete("Students");
        list.clear();
        FirebaseDatabase.getInstance().getReference("Student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Student student = data.getValue(Student.class);
                        list.add(student);
                        Log.e("hello", student.getSeatNo());
                    }
                    Paper.book().write("Students", list);
                    Common.setStudents(list);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void openChooser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle("Select");
        builder.setMessage("Take picture or choose one");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, RC_CAMERA);

            }
        });
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), RC_GALLERY);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog AlertDialog = builder.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case RC_CAMERA:
                if (resultCode == RESULT_OK) {
                    final Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ATTENDACE_IMAGE = bmp;
                    final ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            bmp.recycle();
                            try {
                                stream.close();
                                Intent in = new Intent(Home.this, MarkAttendance.class);
                                in.putExtra("AttedanceImageBytes", byteArray);
                                startActivity(in);
                            } catch (IOException e) {
                                Log.e("IOException", e.getMessage());
                            }

                        }
                    });

                }
                break;

            case 999:
                if (resultCode == 200) {
                    firebaseHelper.getClasses(AppGenericClass.getInstance(Home.this).getPrefs(AppGenericClass.TOKEN));
                }
                break;
            case RC_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri pickedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pickedImage);
                        final Handler handler = new Handler();
                        final Bitmap finalBitmap = bitmap;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Let's read picked image path using content resolver
                                final ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                ATTENDACE_IMAGE = finalBitmap;
                                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                //finalBitmap.recycle();
                                try {
                                    stream.close();
                                    Intent in = new Intent(Home.this, MarkAttendance.class);
                                    in.putExtra("AttedanceImageBytes", byteArray);
                                    startActivity(in);
                                } catch (IOException e) {
                                    Log.e("IOException", e.getMessage());
                                }

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
        }
    }

    @Override
    public void getClasses(List<ClassModel> classModel) {
        Classes = classModel;
        ClassRecyclerView.setAdapter(new ClassRyclerAdapter(Classes, Home.this, Home.this, Home.this));
    }

    @Override
    public void onClassSelected(ClassModel class_) {
        SELECTED_CLASS = class_;
    }
}
