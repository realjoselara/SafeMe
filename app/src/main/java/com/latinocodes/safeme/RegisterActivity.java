package com.latinocodes.safeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.latinocodes.safeme.model.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    //Edit textbox
    EditText regFirstName, regLastName, regEthnicity, regGender, regAge;

    String MyPREFERENCES = "UserSession";
    SharedPreferences sharedpreferences;
    FirebaseDatabase database;
    String devtoken;
    String TAG ="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();

        //set variable for email and password
        regFirstName = findViewById(R.id.regFirstName);
        regLastName = findViewById(R.id.regLastName);
        regEthnicity = findViewById(R.id.regEthnicity);
        regGender = findViewById(R.id.regGender);
        regAge = findViewById(R.id.regAge);

        findViewById(R.id.regActButton).setOnClickListener(this);


        //Testing - Show Token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            devtoken = task.getResult().getToken();

                            //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regActButton:
                //on Register Button - Send User Info to Database
                regUserInfo();

                break;
        }
    }

    private void regUserInfo() {
        //declare variable from the text fields
        String firstname = regFirstName.getText().toString().trim();
        String lastname = regLastName.getText().toString().trim();
        String ethnicity = regEthnicity.getText().toString().trim();
        String gender = regGender.getText().toString().trim();
        String age = regAge.getText().toString().trim();

        try {

            //Validate the fields
            if (firstname.isEmpty()) {
                Toast.makeText(getApplicationContext(), "First Name Required", Toast.LENGTH_SHORT).show();
                regFirstName.requestFocus();
                return;
            }

            if (lastname.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Last Name Required", Toast.LENGTH_SHORT).show();
                regLastName.requestFocus();
                return;
            }

            if (ethnicity.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Ethnicity Required", Toast.LENGTH_SHORT).show();
                regEthnicity.requestFocus();
                return;
            }

            if (gender.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Gender Required. Male or Female?", Toast.LENGTH_SHORT).show();
                regGender.requestFocus();
                return;
            }

            if (age.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Age Required", Toast.LENGTH_SHORT).show();
                return;
            }



        } catch (Exception ex) {
            System.out.println("Error Exception has occurred");
        }

        Gson gson = new Gson();
        String json = sharedpreferences.getString("Userinfo", "");
        User user = gson.fromJson(json, User.class);

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        createuser(uuid, firstname, lastname, ethnicity, gender, age, devtoken);
        getuserinfo(uuid);

        Intent loginScreen = new Intent(this, LoginScreen.class);
        startActivity(loginScreen);

        Toast.makeText(getApplicationContext(),"Registered", Toast.LENGTH_SHORT).show();

    }

    public void createuser(String uuid, String firstname, String lastname, String ethnicity, String gender, String age, String devtoken){
        /***Add user information to the database***/
        //TODO create activity and plug this in
        DatabaseReference user = database.getReference("Users");
        user.child(uuid).child("UUID").setValue(uuid);
        user.child(uuid).child("Firstname").setValue(firstname);
        user.child(uuid).child("Lastname").setValue(lastname);
        user.child(uuid).child("Ethnicity").setValue(ethnicity);
        user.child(uuid).child("Sex").setValue(gender);
        user.child(uuid).child("Age").setValue(age);
        user.child(uuid).child("Token").setValue(devtoken);

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
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.toString());
            }
        });
    }



}
