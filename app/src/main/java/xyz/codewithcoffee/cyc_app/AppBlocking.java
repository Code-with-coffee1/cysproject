package xyz.codewithcoffee.cyc_app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AppBlocking extends AppCompatActivity {

    private static final String TAG = "APP_BLOCK";
    private AppBlocking.AppBlockListAdapter dataAdapter = null;
    private ArrayList<App> applist = null;
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
            },5000);
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
                Toast.makeText(this_context, "Refreshed App List", Toast.LENGTH_SHORT).show();
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
                App pi = (App) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        pi.getName()+" has version "+pi.getVersion(),
                        Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this_context, "Blocked selected Apps", Toast.LENGTH_SHORT).show();
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
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            // skip system apps if they shall not be included
            /*if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                Log.d(TAG,"SYS: "+p.packageName);
                continue;
            }*/
            Log.d(TAG,p.packageName);
            finlist.add(p);
        }
        applist.clear();
        for(PackageInfo pi : finlist)
        {
            App pp = new App();
            pp.setName(pi.applicationInfo.loadLabel(getPackageManager()).toString());
            pp.setCode(pi.packageName);
            pp.setVersion(pi.versionName);
            pp.setIcon(pi.applicationInfo.loadIcon(getPackageManager()));
            pp.setSelected(false);
            applist.add(pp);
        }
    }

    private void getAppList2() throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // get list of all the apps installed
        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
        List<String> componentList = new ArrayList<String>();
        String name = null;
        int i = 0;

        // get size of ril and create a list
        ArrayList<String> apps = new ArrayList<>();
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                // get package
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
                // if activity label res is found
                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);
                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                }
                apps.add(name);
                i++;
            }
        }
    }

    public boolean checkAccessibilityPermission () {
        int accessEnabled = 0;
        try {
            accessEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        // if not construct intent to request permission
        /*Intent intent=new Intent(WebsiteBlocking.this,InstructionsPage.class);
            startActivity(intent);*/
        /*Settings.Secure.putString(getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "xyz.codewithcoffee.cyc_app/UrlInterceptorService");
            Settings.Secure.putString(getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED, "1");*/
        return accessEnabled != 0;
    }


    private class AppBlockListAdapter extends ArrayAdapter<App> {

        private final ArrayList<App> apparray;
        private final ArrayList<String> blist;

        public AppBlockListAdapter(Context context, int textViewResourceId,
                                   ArrayList<App> aalist) {
            super(context, textViewResourceId, aalist);
            this.apparray = new ArrayList<App>();
            this.apparray.addAll(aalist);
            blist = new ArrayList<>();
        }

        private class ViewHolder {
//            TextView code;
            CheckBox name;
            ImageView icon;
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
//                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.site_check);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        App pp = (App) cb.getTag();
                        //PackageInfo pi = pp.getPackageInfo();
                        String status = (cb.isChecked() ? "Marked " : "Unmarked ") + cb.getText() + " for Blocking";
                        pp.setSelected(cb.isChecked());
                        if(cb.isChecked())
                        {
                            blist.add(pp.getCode());
                        }
                        else
                        {
                            blist.remove(pp.getCode());
                        }
                        Toast.makeText(getApplicationContext(),
                                status,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                holder = (AppBlocking.AppBlockListAdapter.ViewHolder) convertView.getTag();
            }

            App pp = apparray.get(position);
//            holder.code.setText(" [" +  pp.getCode() + "]");
            holder.name.setText(pp.getName());
            holder.name.setChecked(pp.isSelected());
            holder.icon.setImageDrawable(pp.getIcon());
            holder.name.setTag(pp);

            return convertView;

        }

    }
}