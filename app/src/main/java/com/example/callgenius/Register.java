package com.example.callgenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private EditText emailfield2;
    private EditText passwordfield2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        emailfield2 = findViewById(R.id.EmailField2);
        passwordfield2 = findViewById(R.id.PasswordField2);

        findViewById(R.id.RegisterButton).setOnClickListener(this);
        findViewById(R.id.SigninSwitchButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
    // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                Toast.makeText(Register.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                updateUI(null);
            }

            // [START_EXCLUDE]
            hideProgressDialog();
            // [END_EXCLUDE]
        }
    });
    // [END create_user_with_email]
}

    private boolean validateForm() {
        boolean valid = true;

        String email = emailfield2.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailfield2.setError("Required.");
            valid = false;
        } else {
            emailfield2.setError(null);
        }

        String password = passwordfield2.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordfield2.setError("Required.");
            valid = false;
        } else {
            passwordfield2.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        boolean success = false;
        if (user != null) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
                success = true;
            }
            if(success){
                startActivity(new Intent(Register.this,MainActivity.class));
                finish();
            }
            else{
                Toast.makeText(this, "Please grant permission in order to use the app. Sign in Again!", Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(this, "Can't find User", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if(i == R.id.RegisterButton){
                createAccount(emailfield2.getText().toString(), passwordfield2.getText().toString());
        }
        else if (i == R.id.SigninSwitchButton){
            startActivity(new Intent(Register.this, SigninEmail.class));
            finish();
        }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
