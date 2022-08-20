package com.aerotom.aerotom.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.aerotom.aerotom.R;
import com.aerotom.aerotom.utils.SessionManager;

import static com.aerotom.aerotom.utils.SessionManager.login;

public class FirstActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sessionManager = new SessionManager(FirstActivity.this);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sessionManager.getBooleanData(login)) {
                        Intent openMainActivity = new Intent(FirstActivity.this, HomeActivity.class);
                        startActivity(openMainActivity);
                        finish();
                    } else {
                        Intent openMainActivity = new Intent(FirstActivity.this, IntroActivity.class);
                        startActivity(openMainActivity);
                        finish();

                    }

                }
            }
        };
        timer.start();
    }
}