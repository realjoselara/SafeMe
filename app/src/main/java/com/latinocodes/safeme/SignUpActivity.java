package com.latinocodes.safeme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare Instance
    private FirebaseAuth mAuth;

    //Edit textbox
    EditText editemailaddress, editpassword, editconfirmpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //set variable for email and password
        editemailaddress = findViewById(R.id.emailfield);
        editpassword = findViewById(R.id.passwordfield);
        editconfirmpass = findViewById(R.id.confirmpass);
        //FireBase Instance
        mAuth = FirebaseAuth.getInstance();
        //Sign up Button waiting for click
        findViewById(R.id.signupbtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupbtn:
                //on signup button - Register the user to Firebase
                regUser();
                break;
        }
    }

    private void regUser() {
        //declare variable from the text fields
        String useremail = editemailaddress.getText().toString().trim();
        String userpass = editpassword.getText().toString().trim();
        String confirmpass = editconfirmpass.getText().toString().trim();

        try {

            //Validate the fields
            if (useremail.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email Required", Toast.LENGTH_SHORT).show();
                editemailaddress.requestFocus();
                return;
            }

            //Regex Email address pattern
            if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()) {
                Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                editemailaddress.requestFocus();
                return;
            }

            //validate password is empty
            if (userpass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Password Required", Toast.LENGTH_SHORT).show();
                editpassword.requestFocus();
                return;
            }
            //validate password length
            if (userpass.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password Length must be at least 6", Toast.LENGTH_SHORT).show();
                editpassword.requestFocus();
                return;
            }

            //validate password with the second textfield
            if (!userpass.equals(confirmpass)) {
                Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                editconfirmpass.requestFocus();
                return;
            }

            //create new user on firebase method
            mAuth.createUserWithEmailAndPassword(useremail, userpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Register Complete", Toast.LENGTH_SHORT).show();
                        Intent backtologin = new Intent(SignUpActivity.this, LoginScreen.class);
                        startActivity(backtologin);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is Registered already", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            System.out.println("Error Exception has occurred");
        }

    }
}