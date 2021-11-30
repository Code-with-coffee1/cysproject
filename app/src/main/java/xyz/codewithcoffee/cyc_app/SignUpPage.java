package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        Spinner spinner =  findViewById(R.id.spinner);
        String[] items = new String[]{"Student","Teacher","Professional"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
    }

    public void log_in_button(View view) {
        Intent intent = new Intent(view.getContext(), LogInPage.class);
        startActivity(intent);
    }
}