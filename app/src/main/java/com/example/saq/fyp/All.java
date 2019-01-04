package com.example.saq.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class All extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView LV;
    String AllActivityString[] = {"MainActivity","Login", "Registration1","Registration2","Home","InClass"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all);

        LV = (ListView)findViewById(R.id.alllv);
        LV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllActivityString));
        LV.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ActivityName  = AllActivityString[position];
        try {
            startActivity(new Intent(this, Class.forName("com.example.saq.fyp." + ActivityName)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
