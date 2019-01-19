package com.example.saq.fyp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppGenericClass {


    public static final String PREFS = "AttrixPrefs";
    public static final String TOKEN= "TOKEN";

    Context context;
    private AppGenericClass(Context context){
        this.context = context;
    }

    public static AppGenericClass getInstance(Context context){
        return new AppGenericClass(context);
    }

    public void showDatePicker(final EditText editText){
        final String selectedDate = "";

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText.setText(year + "-" + month + "-" + day);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(context, listener ,year,month,day);

        dialog.show();
    }

    public void setPrefs(String key,String value){
        context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).edit().putString(key,value).commit();
    }

    public String getPrefs(String key){
        return context.getSharedPreferences(PREFS,Context.MODE_PRIVATE).getString(key,"");
    }

    public String getCurrentYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();

        String year = sdf.format(cal.getTime());
        return year;
    }
}
