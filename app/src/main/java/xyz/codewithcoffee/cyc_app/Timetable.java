package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Timetable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
    }

    public void goto_home_page_navigation(View view) {
        Intent intent=new Intent(view.getContext(), HomePageNav.class);
        startActivity(intent);
    }
}