package xyz.codewithcoffee.cyc_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import xyz.codewithcoffee.cyc_app.databinding.ActivityHomePageNavigationBinding;

public class HomePageNav extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageNavigationBinding binding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);


        binding = ActivityHomePageNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarHomePageNavigation.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.contact:
                        intent=new Intent(HomePageNav.this, ContactUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.chat:
                        intent=new Intent(HomePageNav.this, ChatUI.class);
                        startActivity(intent);
                        return true;
                    case R.id.web_block:
                        intent=new Intent(HomePageNav.this, WebsiteBlocking.class);
                        startActivity(intent);
                        return true;
                    case R.id.app_block:
                        intent=new Intent(HomePageNav.this, AppBlocking.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_home:
                        intent=new Intent(HomePageNav.this, HomePageNav.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_gallery:
                        intent=new Intent(HomePageNav.this, AboutUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.Update_timetable:
                        intent=new Intent(HomePageNav.this, Timetable.class);
                        startActivity(intent);
                        return true;
                    case R.id.rate:
                        intent=new Intent(HomePageNav.this, RateUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.share:
                        intent=new Intent(HomePageNav.this, ShareApp.class);
                        startActivity(intent);
                        return true;
                    case R.id.signout:
                        AuthUI.getInstance().signOut(HomePageNav.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(HomePageNav.this,
                                                "You have been signed out.",
                                                Toast.LENGTH_LONG)
                                                .show();
                                        Intent intent2=new Intent(HomePageNav.this, LogInPage.class);
                                        startActivity(intent2);
                                        // Close activity
                                        finish();
                                    }
                                });
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page_navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page_navigation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}