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
        switch(v.getId()){
            case R.id.signupbtn:
                //on signup button - Register the user to Firebase
                regUser();
                break;
        }
    }
    private void regUser(){
        //declare variable from the text fields
        String useremail = editemailaddress.getText().toString().trim();
        String userpass = editpassword.getText().toString().trim();
        String confirmpass = editconfirmpass.getText().toString().trim();

        //Validate the fields
        if(useremail.isEmpty()) {
            editemailaddress.setError("Email Required");
            editemailaddress.requestFocus();
            return;
        }

        //Regex Email address pattern
        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            editemailaddress.setError("Enter Valid Email Address");
            editemailaddress.requestFocus();
            return;
        }

        //validate password is empty
        if(userpass.isEmpty()){
            editpassword.setError("Password Required");
            editpassword.requestFocus();
            return;
        }
        //validate password length
        if(userpass.length()<6){
            editpassword.setError("Minimum length should be 6");
            editpassword.requestFocus();
            return;
        }

        //validate password with the second textfield
        if(!userpass.equals(confirmpass)){
            editconfirmpass.setError("Password does not match");
            editconfirmpass.requestFocus();
            return;
        }

        //create new user to firebase method
        mAuth.createUserWithEmailAndPassword(useremail, userpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Register Complete", Toast.LENGTH_SHORT).show();
                    Intent backtologin = new Intent(SignUpActivity.this, LoginScreen.class);
                    startActivity(backtologin);
                } else{
                    Toast.makeText(getApplicationContext(), "Email is Registered already", Toast.LENGTH_SHORT).show();
                }
                }
            });

        }

    }


