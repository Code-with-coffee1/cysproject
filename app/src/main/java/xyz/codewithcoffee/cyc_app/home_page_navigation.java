package xyz.codewithcoffee.cyc_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import xyz.codewithcoffee.cyc_app.databinding.ActivityHomePageNavigationBinding;

public class home_page_navigation extends AppCompatActivity {

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

        setSupportActionBar(binding.appBarHomePageNavigation.toolbar);

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
                switch (item.getItemId()){
                    case R.id.contact:
                        Intent intent=new Intent(home_page_navigation.this, ContactUs.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_home:
                        Intent intent2=new Intent(home_page_navigation.this, home_page_navigation.class);
                        startActivity(intent2);
                        return true;
                    case R.id.nav_gallery:
                        Intent intent3=new Intent(home_page_navigation.this, AboutUs.class);
                        startActivity(intent3);
                        return true;
                    case R.id.Update_timetable:
                        Intent intent4=new Intent(home_page_navigation.this, Timetable.class);
                        startActivity(intent4);
                        return true;
                    case R.id.rate:
                        Intent intent5=new Intent(home_page_navigation.this, RateUs.class);
                        startActivity(intent5);
                        return true;
                    case R.id.share:
                        Intent intent6=new Intent(home_page_navigation.this, ShareApp.class);
                        startActivity(intent6);
                        return true;
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