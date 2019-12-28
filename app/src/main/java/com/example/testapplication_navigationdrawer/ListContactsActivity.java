package com.example.testapplication_navigationdrawer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ListContactsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ExpandableListView expandableListView;
    List<String> headerList;
    HashMap<String, List<String>> childList;
    CustomExpandableAdapter customExpandableAdapter;
    int lastExpandedPosition = -1;
    private static final int REQUEST_CALL = 1;
    private AlertDialog.Builder alertdialog;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        bundle = getIntent().getExtras();

        //Setting Custom Toolbar
        toolbar = findViewById(R.id.activity_listcontact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(bundle.getString("division"));

        //Adding back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        expandableListView = findViewById(R.id.expandableListViewId);

        loadEmergencyContacts();

        customExpandableAdapter = new CustomExpandableAdapter(this, headerList, childList);
        expandableListView.setAdapter(customExpandableAdapter);

        //If clicked another group while one is expanded,
        //collapse that group
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && lastExpandedPosition != groupPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String num = childList.get(headerList.get(groupPosition)).get(childPosition);

                //AlertDialog
                alertdialog = new AlertDialog.Builder(ListContactsActivity.this);
                alertdialog.setTitle("Call Number");
                alertdialog.setMessage(num);
                alertdialog.setIcon(R.drawable.phone);
                alertdialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Request for permission
                        ActivityCompat.requestPermissions(ListContactsActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                        callNumber(num);
                    }
                });
                alertdialog.setNegativeButton("Cancel", null);
                alertdialog.setCancelable(true);

                alertdialog.create().show();
                return false;
            }
        });

    }

    public void callNumber(String num){
        if(ContextCompat.checkSelfPermission(ListContactsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ListContactsActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else{
            String dial = "tel:" + num.trim();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void loadEmergencyContacts() {
        Resources res = getResources();

        InputStream is = res.openRawResource(R.raw.emergency_contacts);

        Scanner scanner = new Scanner(is);

        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        headerList = new ArrayList<>();
        childList = new HashMap<>();

        try {
            JSONObject root = new JSONObject(s);

            //get which division

            JSONObject place = root.getJSONObject(bundle.getString("division"));

            headerList.add("Ambulance");
            JSONArray ambulance = place.getJSONArray("ambulance");
            List<String> child = new ArrayList<>();
            for(int i = 0; i < ambulance.length(); i++) {
                child.add(ambulance.get(i).toString());
            }
            childList.put("Ambulance", child);

            headerList.add("Police");
            JSONArray police = place.getJSONArray("police");
            child = new ArrayList<>();
            for(int i = 0; i < police.length(); i++) {
                child.add(police.get(i).toString());
            }
            childList.put("Police", child);

            headerList.add("Fire Service");
            JSONArray fire = place.getJSONArray("fire");
            child = new ArrayList<>();
            for(int i = 0; i < fire.length(); i++) {
                child.add(fire.get(i).toString());
            }
            childList.put("Fire Service", child);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
