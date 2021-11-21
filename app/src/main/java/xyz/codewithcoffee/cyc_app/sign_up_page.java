package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import xyz.codewithcoffee.cyc_app.R;

public class sign_up_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
    }

    public void log_in_button(View view) {
        Intent intent = new Intent(view.getContext(), log_in_page.class);
        startActivity(intent);
    }
}