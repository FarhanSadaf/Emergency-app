package com.example.testapplication_navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ContactActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> divisions;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        //Setting Custom Toolbar
        toolbar = findViewById(R.id.activity_contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Emergency Contacts");

        //Adding back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.contactListView);

        //Get division names
        divisions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.divison_list)));

        CustomAdapter customAdapter = new CustomAdapter(ContactActivity.this, divisions, R.drawable.bangladesh);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactActivity.this, ListContactsActivity.class);
                intent.putExtra("division", divisions.get(position));
                startActivity(intent);
            }
        });
    }
}
