package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
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
        Context this_context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_blocking);
        Button refresh = findViewById(R.id.refresh_applist);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppList();
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

    private class AppBlockListAdapter extends ArrayAdapter<PackageInfo> {

        private ArrayList<PackageInfo> apparray;

        public AppBlockListAdapter(Context context, int textViewResourceId,
                                   ArrayList<PackageInfo> SiteList) {
            super(context, textViewResourceId, SiteList);
            this.apparray = new ArrayList<PackageInfo>();
            this.apparray.addAll(SiteList);
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