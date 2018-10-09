package com.latinocodes.safeme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginScreen extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        button = findViewById(R.id.loginButton);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSafeMeActivity();
            }
        });
    }
    public void openSafeMeActivity(){
        Intent intent = new Intent(this, SafeMeActivity.class);
        startActivity(intent);
    }

}
