package com.example.testapplication_navigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button signUpButton;
    TextView signUpTextView;
    EditText signUpEmailEditText, signUpPasswordEditText, signUpPasswordReenterEditText;
    ProgressDialog loadingBar;
    Toolbar toolbar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Setting Custom Toolbar
        toolbar = findViewById(R.id.activity_signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signupButton);
        signUpTextView = findViewById(R.id.signupTextView);
        signUpEmailEditText = findViewById(R.id.signupEmailEditText);
        signUpPasswordEditText = findViewById(R.id.signupPasswordEditText);
        signUpPasswordReenterEditText = findViewById(R.id.signupReenterPasswordEditText);

        loadingBar = new ProgressDialog(this);

        signUpButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signupButton:
                registerUser();
                break;

            case  R.id.signupTextView:
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void registerUser() {
        String email = signUpEmailEditText.getText().toString().trim();
        String password = signUpPasswordEditText.getText().toString().trim();
        String password2 = signUpPasswordReenterEditText.getText().toString().trim();

        //Checking validity of email
        if(email.isEmpty()){
            signUpEmailEditText.setError("Enter an email address");
            signUpEmailEditText.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmailEditText.setError("Enter a valid email address");
            signUpEmailEditText.requestFocus();
            return;
        }

        //Checking validity of password
        if(password.isEmpty()){
            signUpPasswordEditText.setError("Enter a password");
            signUpPasswordEditText.requestFocus();
            return;
        }
        if(password.length() < 6){
            signUpPasswordEditText.setError("Length must be minimum of length 6");
            signUpPasswordEditText.requestFocus();
            return;
        }

        if(!password.equals(password2)){
            signUpPasswordReenterEditText.setError("Password don't match");
            signUpPasswordReenterEditText.requestFocus();
            return;
        }

        //VALID
        loadingBar.setTitle("Creating New Account");
        loadingBar.setMessage("Please wait...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingBar.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "SignUp successful", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "User already exists!\nPlease Sign In", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
