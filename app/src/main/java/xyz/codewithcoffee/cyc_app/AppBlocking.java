package xyz.codewithcoffee.cyc_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import xyz.codewithcoffee.cyc_app.Adapter.AppAdapters;
import xyz.codewithcoffee.cyc_app.Utlis.Utlis;

import java.util.ArrayList;
import java.util.List;

public class AppBlocking extends AppCompatActivity {
    LinearLayout layout_permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_blocking);


        intitView();
    }

    private void intitView() {

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppAdapters appAdapters= new AppAdapters(this,getAllApps());
        recyclerView.setAdapter(appAdapters);

        layout_permission=findViewById(R.id.layout_permission);
    }

    private List<Appitem> getAllApps() {

        List<Appitem> results= new ArrayList<>();
        PackageManager pk= getPackageManager();

        Intent intent=new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);


        List<ResolveInfo> resolveInfoList=pk.queryIntentActivities(intent,0);

        for (ResolveInfo resolveInfo:resolveInfoList){
            ActivityInfo activityInfo=resolveInfo.activityInfo;

            results.add(new Appitem(activityInfo.loadIcon(pk),activityInfo.loadLabel(pk).toString(),activityInfo.packageName));
        }


        return results;
    }

//    private void initToolbar() {
//        Toolbar toolbar=findViewById(R.id.toolbar);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.home){
            finish();
        }
        return true;
    }

    public void setPermission(View view) {
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @Override
    protected void onResume() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            if (Utlis.checkPermission(this)){
                layout_permission.setVisibility(View.GONE);
            }else {
//                layout_permission.setVisibility(View.INVISIBLE);
            }
        }

        super.onResume();
    }
}