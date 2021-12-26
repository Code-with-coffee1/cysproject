package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppBlocking extends AppCompatActivity {

    private static final String TAG = "APP_BLOCK";
    private AppBlocking.AppBlockListAdapter dataAdapter = null;
    private ArrayList<PackageInfo> applist = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!MainActivity.isAccessibilityServiceEnabled(this, AppMonitorService.class)){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_instructions_page);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // request permission via start activity for result
                    startActivity(intent);
                    finish();
                }
            },10000);
        }
        else
        {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_app_blocking);
            setOnclicks();
        }
    }

    private void setOnclicks()
    {
        Context this_context = this;
        Button refresh = findViewById(R.id.refresh_applist);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppList();
                Toast.makeText(this_context, "Refreshed App List", Toast.LENGTH_LONG).show();
                dataAdapter = new AppBlocking.AppBlockListAdapter(this_context,
                        R.layout.listitem, applist);
                listView.setAdapter(dataAdapter);
            }
        });
        applist = new ArrayList<>();
        dataAdapter = new AppBlocking.AppBlockListAdapter(this,
                R.layout.listitem, applist);
        listView = (ListView) findViewById(R.id.blocked_apps);
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PackageInfo pi = (PackageInfo) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        pi.packageName+" has version "+pi.versionName,
                        Toast.LENGTH_LONG).show();
            }
        });
        Button bblock = (Button)findViewById(R.id.block_app_butt);
        bblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyServiceRunning(AppMonitorService.class))
                {
                    stopService(new Intent(AppBlocking.this, AppMonitorService.class));
                }
                Toast.makeText(this_context, "Blocked selected Apps", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppBlocking.this, AppMonitorService.class);
                intent.putExtra("applist", dataAdapter.blist);
                startService(intent);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void getAppList()
    {
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<PackageInfo> finlist = new ArrayList<>();
        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            // skip system apps if they shall not be included
            if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            finlist.add(p);
        }
        applist = finlist;
    }

    public boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessEnabled == 0) {
            // if not construct intent to request permission
            /*Intent intent=new Intent(WebsiteBlocking.this,InstructionsPage.class);
            startActivity(intent);*/
            return false;
        } else {
            /*Settings.Secure.putString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "xyz.codewithcoffee.cyc_app/UrlInterceptorService");
            Settings.Secure.putString(getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED, "1");*/
            return true;
        }
    }


    private class AppBlockListAdapter extends ArrayAdapter<PackageInfo> {

        private ArrayList<PackageInfo> apparray;
        private ArrayList<String> blist;

        public AppBlockListAdapter(Context context, int textViewResourceId,
                                   ArrayList<PackageInfo> SiteList) {
            super(context, textViewResourceId, SiteList);
            this.apparray = new ArrayList<PackageInfo>();
            this.apparray.addAll(SiteList);
            blist = new ArrayList<>();
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            AppBlocking.AppBlockListAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listitem, null);

                holder = new AppBlocking.AppBlockListAdapter.ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.site_check);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        PackageInfo pi = (PackageInfo) cb.getTag();
                        String status = (cb.isChecked() ? "Marked " : "Unmarked ") + cb.getText() + " for Blocking";
                        if(cb.isChecked())
                        {
                            blist.add(pi.packageName);
                        }
                        else
                        {
                            blist.remove(pi.packageName);
                        }
                        Toast.makeText(getApplicationContext(),
                                status,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                holder = (AppBlocking.AppBlockListAdapter.ViewHolder) convertView.getTag();
            }

            PackageInfo pi = apparray.get(position);
            holder.code.setText(" [" +  pi.versionName + "]");
            holder.name.setText(pi.packageName);
            holder.name.setChecked(false);
            holder.name.setTag(pi);

            return convertView;

        }

    }
}