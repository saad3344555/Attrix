package com.example.saq.fyp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FirebaseHelper {

    DatabaseReference dbRef;
    Context context;
    SignInCallBack signInCallBack;


    public void setStoreImageCallBack(StoreImageCallBack storeImageCallBack) {
        this.storeImageCallBack = storeImageCallBack;
    }

    StoreImageCallBack storeImageCallBack;
    RegisterCallBack registerCallBack;
    ClassCallback classCallback;
    GetClassCallback getClassCallback;

    public interface ClassCallback{
        void onCreated(int code);
    }

    public interface GetClassCallback{
        void getClasses(List<ClassModel> classModel);
    }

    public interface StoreImageCallBack{
        void onSuccess(Uri uri);
    }

    public interface SignInCallBack {
        void onSignIn(int code,String id);
    }

    public interface RegisterCallBack {
        void onRegister(boolean success,String id);
    }


    public void setClassCallback(ClassCallback classCallback) {
        this.classCallback = classCallback;
    }

    public void setSignInCallBack(SignInCallBack signInCallBack) {
        this.signInCallBack = signInCallBack;
    }

    public void setRegisterCallBack(RegisterCallBack registerCallBack) {
        this.registerCallBack = registerCallBack;
    }


    public void setGetClassCallback(GetClassCallback getClassCallback) {
        this.getClassCallback = getClassCallback;
    }

    public FirebaseHelper(Context context) {
        FirebaseApp.initializeApp(context);
        this.dbRef = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

//
//    public void uploadImage(final Uri image){
//        storageRef = FirebaseStorage.getInstance().getReference();
//        final StorageReference ref = storageRef.child("Student Images"+ image.getLastPathSegment());
//        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
//        dialog.setMessage("Uploading File. Please wait...");
//        dialog.show();
//
//        Task<Uri> uploadTask = ref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
//                Log.d("Exception",e.toString());
//            }
//        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return ref.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    storeImageCallBack.onSuccess(downloadUri);
//                } else {
//                    Log.d("Failed Download","something went wrong");
//                }
//            }
//        });
//    }

    public void registerTeacher(final SignInUpModel signInUpModel) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Registering. Please wait...");
        dialog.show();

        dbRef.child("Teacher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();
                boolean exists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignInUpModel model = snapshot.getValue(SignInUpModel.class);
                    exists = signInUpModel.getMobile().equals(model.getMobile());
                    if (exists)
                        break;
                }

                if (!exists) {
                    String key = dbRef.child("Teacher").push().getKey();
                    signInUpModel.setId(key);
                    dbRef.child("Teacher").child(key).setValue(signInUpModel);
                    registerCallBack.onRegister(true,key);
                } else
                    registerCallBack.onRegister(false,null);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Log.d("dbError", databaseError.getMessage());
            }
        });
    }



    public void createClass(final ClassModel classModel) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Creating Class. Please wait...");
        dialog.show();
        dbRef.child("Class").push().setValue(classModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                classCallback.onCreated(200);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("ClassCreation Error",e.toString());
                e.printStackTrace();
                classCallback.onCreated(202);
            }
        });

    }

    public void signInTeacher(final String mobileNo, final String pass) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Logging In. Please wait...");
        dialog.show();
        dbRef.child("Teacher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();

                boolean mobileMatch;
                boolean passMatch;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    SignInUpModel model = snapshot.getValue(SignInUpModel.class);

                    mobileMatch = mobileNo.equals(model.getMobile());
                    passMatch = pass.equals(model.getPass());

                    if (mobileMatch && passMatch) {
                        signInCallBack.onSignIn(200,model.getId());
                        return;
                    } else if (mobileMatch && !passMatch) {
                        signInCallBack.onSignIn(201,null);
                        return;
                    }
                }

                signInCallBack.onSignIn(202,null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Log.d("dbError", databaseError.getMessage());
            }
        });
    }

    public void getClasses(final String teacherId) {

        final AlertDialog dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Getting classes. Please wait...");
        dialog.show();
        dbRef.child("Class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dialog.dismiss();

                List<ClassModel> classModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getValue(ClassModel.class).getTeacherId().equals(teacherId)){
                        classModels.add(snapshot.getValue(ClassModel.class));
                    }
                }

                getClassCallback.getClasses(classModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Log.d("dbError", databaseError.getMessage());
            }
        });
    }
}
