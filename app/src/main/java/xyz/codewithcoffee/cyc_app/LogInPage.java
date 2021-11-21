package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void sign_up_button(View view) {
        Intent intent = new Intent(view.getContext(), SignUpPage.class);
        startActivity(intent);
    }

    public void goto_websiteblocking(View view){
        Intent intent = new Intent(view.getContext(), WebsiteBlocking.class);
        startActivity(intent);
    }
}