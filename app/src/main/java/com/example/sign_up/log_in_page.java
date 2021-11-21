package com.example.sign_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class log_in_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void sign_up_button(View view) {
        Intent intent = new Intent(view.getContext(), sign_up_page.class);
        startActivity(intent);
    }

    public void goto_websiteblocking(View view){
        Intent intent = new Intent(view.getContext(), website_blocking.class);
        startActivity(intent);
    }
}