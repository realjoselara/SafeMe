package com.latinocodes.safeme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    private Button button;
    private Button signButton;

    //Firebase Generated Code
    FirebaseAuth mAuth;

    EditText editemailaddress, editpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();
        editemailaddress = findViewById(R.id.loginEmailAddress);
        editpassword = findViewById(R.id.loginpassword);


        button = findViewById(R.id.loginButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logintosafeme();

            }
        });

        signButton = findViewById(R.id.registerbtn);
        signButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();

            }
        });
    }
    private void logintosafeme(){
        try {
            //get email and password from the two text fields
            //declare variable from the text fields
            String useremail = editemailaddress.getText().toString().trim();
            String userpass = editpassword.getText().toString().trim();

            //validate text fields: Email address and Password
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
                Toast.makeText(getApplicationContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                editpassword.requestFocus();
                return;
            }

            //sign in method from FirebaseAuth
            mAuth.signInWithEmailAndPassword(useremail, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Login Complete", Toast.LENGTH_SHORT).show();

                        openSafeMeActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check Email Address or Password", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }catch(Exception ex){
            System.out.println("Error Exception has occurred");
        }
    }
    public void openSafeMeActivity(){
        Intent intent = new Intent(this, SafeMeActivity.class);
        startActivity(intent);
    }
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
