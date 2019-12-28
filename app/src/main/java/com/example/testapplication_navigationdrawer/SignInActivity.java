package com.example.testapplication_navigationdrawer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    Button signInButton;
    TextView signInTextView;
    EditText signInEmailEditText, signInPasswordEditText;
    ImageButton imageButton;
    ProgressDialog loadingBar;
    Toolbar toolbar;

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_SMS = 0;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Setting Custom Toolbar
        toolbar = findViewById(R.id.activity_signin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign In");

        requestPermissions();

        mAuth = FirebaseAuth.getInstance();

        signInButton = findViewById(R.id.signinButton);
        signInTextView = findViewById(R.id.signinTextView);
        signInEmailEditText = findViewById(R.id.signinEmailEditText);
        signInPasswordEditText = findViewById(R.id.signinPasswordEditText);
        imageButton = findViewById(R.id.showPasswordImageButton_signin);

        loadingBar = new ProgressDialog(this);

        imageButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showPasswordImageButton_signin:
                signInPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;

            case R.id.signinButton:
                loginUser();
                break;

            case  R.id.signinTextView:
                finish();
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginUser() {
        String email = signInEmailEditText.getText().toString().trim();
        String password = signInPasswordEditText.getText().toString().trim();

        //Checking validity of email
        if(email.isEmpty()){
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }

        //Checking validity of password
        if(password.isEmpty()){
            signInPasswordEditText.setError("Enter a password");
            signInPasswordEditText.requestFocus();
            return;
        }
        if(password.length() < 6){
            signInPasswordEditText.setError("Length must be minimum of length 6");
            signInPasswordEditText.requestFocus();
            return;
        }

        //VALID
        loadingBar.setTitle("Signing In");
        loadingBar.setMessage("Please wait...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingBar.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Sign In Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestPermissions() {
        if(ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SignInActivity.this, new String[] {Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

}
