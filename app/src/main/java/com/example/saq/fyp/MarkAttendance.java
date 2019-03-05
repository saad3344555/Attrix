package com.example.saq.fyp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saq.fyp.model.Attendance;
import com.google.firebase.database.FirebaseDatabase;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.contract.TrainingStatus;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MarkAttendance extends AppCompatActivity {
    private TextView bt_markAttendance;
    private ImageView iv_pic;
    private Toolbar toolbar;
    private EditText et_date;
    private RecyclerView rv_attendance;
    byte[] imageBytes = {};
    Face facesDetected[];
    String personGroupId;
    List<Attendance> attendanceList;
    ProgressDialog progressDialog;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private FaceServiceRestClient faceServiceRestClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "ed27c09cdaf94afabbbbda6492099350");

    List<AttendanceModel> attendanceModels = new ArrayList<>();
    private String date = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        personGroupId = Home.SELECTED_CLASS.getYearOfTeaching();
        setAttendanceList(Home.SELECTED_CLASS.getEnrolledStudents());

        Log.e("GroupId", personGroupId);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mark Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // imageBytes = getIntent().getByteArrayExtra("AttedanceImageBytes");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Marking Attendance");
        progressDialog.setMessage("Please Wait");

        iv_pic = findViewById(R.id.iv_pic);
        iv_pic.setImageBitmap(Home.ATTENDACE_IMAGE);

        bt_markAttendance = findViewById(R.id.bt_markAttendance);
        bt_markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectAndFrame(Home.ATTENDACE_IMAGE);
            }
        });

        et_date = findViewById(R.id.et_date);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    private void showDatePicker() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = year + "-" + (month + 1) + "-" + day;
                et_date.setText(date);
                //callService(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);

        dialog.show();
    }


    private void setAttendanceList(List<String> enrolledStudents) {
        attendanceList = new ArrayList<>();
        for (String face_id : enrolledStudents) {
            attendanceList.add(new Attendance(face_id, false));
        }
        //FirebaseDatabase.getInstance().getReference("Attendance").child(todayDate).setValue(attendanceList);
    }


    private void detectAndFrame(final Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        new starterTask().execute(byteArrayInputStream);
    }

    private class starterTask extends AsyncTask<InputStream, String, Face[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected Face[] doInBackground(InputStream... inputStreams) {
            publishProgress("Detecting...");
            try {
                Face[] faces = faceServiceRestClient.detect(inputStreams[0], true, false, null);
                if (faces == null) {
                    publishProgress("Unable to detect!");
                    progressDialog.dismiss();
                    return null;
                }
                publishProgress(String.format("%s face(s) detected!", faces.length));
                //Toast.makeText(MainActivity.this, ""+faces.length+" faces detected!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return faces;
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                progressDialog.dismiss();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Face[] faces) {
            if (faces == null) return;
            facesDetected = new Face[faces.length];
            facesDetected = faces;
            Log.e(MainActivity.class.getSimpleName(), faces.length + " face(s) detected!");
            UUID[] facesId = new UUID[facesDetected.length];
            for (int i = 0; i < facesDetected.length; i++) {
                facesId[i] = facesDetected[i].faceId;
                Log.e("FaceId", facesDetected[i].faceId + "");
            }
            new identificationTask(personGroupId).execute(facesId);
            // imageView.setImageBitmap(drawRectsOnFaces(bitmap, faces));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class identificationTask extends AsyncTask<UUID, String, IdentifyResult[]> {

        String personGroupId;
        private ProgressDialog progressDialog = new ProgressDialog(MarkAttendance.this);


        public identificationTask(String personGroupId) {
            this.personGroupId = personGroupId;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(IdentifyResult[] identifyResult) {
            progressDialog.dismiss();

            try {
                Log.e("Size", identifyResult.length + "");
                if (identifyResult.length > 0) {
                    for (IdentifyResult result : identifyResult) {

                        try {
                            //Log.e("candidates", String.valueOf(result.candidates.size()));
                            Log.e("personId", result.candidates.get(0).personId + "");
                            updateStudentAttendaceStatus(result.candidates.get(0).personId);
                            //save person id in array here
                            new PersonDetectTask(this.personGroupId).execute(result.candidates.get(0).personId);
                        } catch (Exception e) {
                            Log.e("Taskcandidates", e.getMessage());
                        }

                    }
                } else
                    Log.e(MainActivity.class.getSimpleName(), "IdentifyArray is empty");
            } catch (Exception e) {
                Log.e("EceptionIdentify", e.getMessage());
            }


        }

        @Override
        protected IdentifyResult[] doInBackground(UUID... uuids) {
            try {
                publishProgress("Getting person group status...");
                TrainingStatus trainingStatus = faceServiceRestClient.getPersonGroupTrainingStatus(this.personGroupId);
                Log.e("trainingStatus", trainingStatus.status + " ");
                progressDialog.dismiss();

                if (trainingStatus.status != TrainingStatus.Status.Succeeded) {
                    publishProgress("Person group training status is " + trainingStatus.status);
                    return null;
                }
                publishProgress("Identifying...");


                return faceServiceRestClient.identity(this.personGroupId, uuids, 1);

            } catch (ClientException e) {
                Log.e("ClientException", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
        }
    }

    private void updateStudentAttendaceStatus(UUID personId) {
        for (Attendance model : attendanceList) {
            if (model.getFace_id().equals(personId.toString())) {
                model.setIs_present(true);
                break;
            }
        }
    }

    private class PersonDetectTask extends AsyncTask<UUID, String, Person> {

        String personGroupId;
        ProgressDialog dialog = new ProgressDialog(MarkAttendance.this);

        public PersonDetectTask(String personGroupId) {
            this.personGroupId = personGroupId;
        }


        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(Person person) {
            iv_pic.setImageBitmap(drawRectOnImage(Home.ATTENDACE_IMAGE, facesDetected, person.name));
            openEditAttendanceActivity();
            bt_markAttendance.setBackground(getDrawable(R.drawable.socialroundedbutton));
            bt_markAttendance.setText("Attendance Marked!");
            bt_markAttendance.setTextColor(Color.WHITE);
            bt_markAttendance.setEnabled(false);
        }

        @Override
        protected Person doInBackground(UUID... uuids) {
            try {
                // publishProgress("Getting person group status...");

                Log.e("id", uuids[0] + "");

                Person person = faceServiceRestClient.getPerson(this.personGroupId, uuids[0]);
                dialog.dismiss();
                Log.e("personname", person.name);
                // names = names.concat(person.name + " ");
                return person;
            } catch (ClientException e) {
                Log.e("ClientException", e.getMessage());
                return null;


            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return null;
            }
        }
    }

    private void openEditAttendanceActivity() {
        Intent intent = new Intent(MarkAttendance.this, EditAttendance.class);

    }

    private Bitmap drawRectOnImage(Bitmap bitmap, Face[] facesDetected, String name) {
        Log.e(MainActivity.class.getSimpleName(), "inside rectangel");
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);

        if (facesDetected != null) {
            for (Face face : facesDetected
                    ) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(faceRectangle.left, faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.height + faceRectangle.top, paint);

                // drawNameOnCanvas(canvas, 50, ((faceRectangle.width + faceRectangle.left) / 2) + 100, ((faceRectangle.top + faceRectangle.height) / 2) + 50, Color.WHITE, name);

//                textView.setVisibility(View.VISIBLE);
//                textView.setText(names);
            }
        }
        markAttendanceToFB();
        return copy;
    }

    private void markAttendanceToFB() {

        AttendanceModel attendanceModel = new AttendanceModel();
        attendanceModel.setAttendanceList(attendanceList);
        attendanceModel.setProgram(Home.SELECTED_CLASS.getProgram());
        attendanceModel.setSectionName(Home.SELECTED_CLASS.getSection());
        attendanceModel.setShift(Home.SELECTED_CLASS.getShift());
        attendanceModel.setCourseNo(Home.SELECTED_CLASS.getCourseNo());
        attendanceModel.setYear(Home.SELECTED_CLASS.getYearOfTeaching());
        FirebaseDatabase.getInstance().getReference("Attendance").child(Home.SELECTED_CLASS.getClassId()).child(date).setValue(attendanceModel);
    }
}
