package com.example.testapplication_navigationdrawer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_SMS = 0;
    private static final int REQUEST_CALL = 1;

    private String locationUrl = "http://www.google.com/maps/place/";
    private String message, currentUserId;
    private ArrayList<String> phonenumbers;
    private FirebaseDatabase database;
    private DatabaseReference myRef, myRefName;

    private TextView countTextView;
    private Button stopButton;
    Vibrator vibrator;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getUid();

        countTextView = findViewById(R.id.countTextView);
        stopButton = findViewById(R.id.stopButton);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        myRef = database.getReference(currentUserId).child("phones");
        myRefName = database.getReference(currentUserId).child("name");

        updateNumbers();
        createMessage();

        count = loadTimeFromSharedPreferences();
        countTextView.setText(count+"");
        vibrate();

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(count == 0){
                                    stopButton.setEnabled(false);
                                    interrupt();

                                    //Make an emergency call
                                    Log.d("tag", message);
                                    if(loadFunctionsFromSharedPreference("call"))
                                        callEmergency();

                                    if(loadFunctionsFromSharedPreference("message"))
                                        sendMessages();

                                    finish();
                                }
                                else {
                                    count--;
                                    countTextView.setText(count+"");
                                    vibrate();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                countTextView.setText("Stopped");
                finish();
            }
        });
    }

    private int loadTimeFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("timer", MODE_PRIVATE);
        return sharedPreferences.getInt("time", 5);
    }

    private boolean loadFunctionsFromSharedPreference(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("function", MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, true);
    }

    private String loadLocationFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("location_url", MODE_PRIVATE);
        return sharedPreferences.getString("location", null);
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else{
            vibrator.vibrate(500);
        }
    }

    private void sendMessages() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS);
        }

        StringBuilder numbers = new StringBuilder();
        for (String number: phonenumbers){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);

            numbers.append(number).append("\n");
        }
        Toast.makeText(this, "Message sent to:\n" + numbers.toString(), Toast.LENGTH_SHORT).show();
    }

    public void callEmergency(){
        if(ContextCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EmergencyActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        //make call to 999
        else{
            String dial = "tel:" + "999";
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    locationUrl = locationUrl + location.getLatitude() + "," + location.getLongitude();
                    message = message + "Location: " + locationUrl;
                }
                else{
                    Log.d("tag", "problem fetching location");
                    message = message + "Previous location: " + loadLocationFromSharedPreferences();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;

            case REQUEST_CODE_SMS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    sendMessages();
                }
                break;

            case REQUEST_CALL:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callEmergency();
                }
                break;
        }
    }

    private void updateNumbers() {

        phonenumbers = new ArrayList<String>();


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String phone = dataSnapshot.getValue(String.class);
                phonenumbers.add(phone);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String phone = dataSnapshot.getValue(String.class);
                phonenumbers.remove(phone);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void createMessage() {
        myRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user = dataSnapshot.getValue(String.class);
                message = user + " is in DANGER!\n";
                fetchLastLocation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
