package com.latinocodes.safeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.latinocodes.safeme.model.User;

public class LoginScreen extends AppCompatActivity {
    private Button button;
    private Button signButton;

    //Firebase Generated Code
    FirebaseAuth mAuth;
    EditText editemailaddress, editpassword;
    SharedPreferences sharedpreferences;
    String MyPREFERENCES = "UserSession";
    FirebaseDatabase database;
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        database = FirebaseDatabase.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // get current user after successful login
                        getuserinfo(user.getUid()); // get user info from DB
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

    public void getuserinfo(final String uuid){
        /***Retrieve user information from DB and store data to Shared preferences available through
         * out the application***/

        DatabaseReference my = database.getReference("Users");
        DatabaseReference userdata = my.child(uuid);
        userdata.addValueEventListener(new ValueEventListener() {
//            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                user.setFirstName(dataSnapshot.child("Firstname").getValue(String.class));
                user.setLastName(dataSnapshot.child("Lastname").getValue(String.class));
                user.setSex(dataSnapshot.child("Sex").getValue(String.class));
                user.setUserID(uuid);
                user.setEthnicity(dataSnapshot.child("Ethnicity").getValue(String.class));

                //add user information to Shared preferences using editor and gson
                SharedPreferences.Editor editor = sharedpreferences.edit(); //create editor object
                Gson gson = new Gson();
                String json = gson.toJson(user); //convert User obj to json format to be stored

                editor.putString("Userinfo", json); // store to shared preferences as Userinfo to be retirived later.
                editor.commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }



    public boolean createuser(){
        /***Add user information to the database***/
        //TODO create activity and plug this in
        DatabaseReference user = database.getReference("Users");
        user.child("UUID").setValue("");
        user.child("Firstname").setValue("");
        user.child("Lastname").setValue("");
        user.child("Age").setValue("");
        user.child("Sex").setValue("");
        user.child("Ethnicity").setValue("");
        user.child("LastLocation").setValue("");


        return true;
    }
}
