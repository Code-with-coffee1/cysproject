package xyz.codewithcoffee.cyc_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class WebsiteBlocking extends AppCompatActivity {

    private MyCustomAdapter dataAdapter = null;
    private ArrayList<Site> SiteList = null;
    private ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_blocking);
        SiteList = new ArrayList<Site>();
        funcInit();
    }

    private void funcInit() {
        /*Site loc = new Site("BLOCKED","127.0.0.1",false);
        SiteList.add(loc);
        Site fb = new Site("BLOCKED","https://facebook.com",false);
        SiteList.add(fb);
        Site goog = new Site("FAILED","https://google.com",false);
        SiteList.add(goog);
        Site yt = new Site("BLOCKED","https://youtube.com",false);
        SiteList.add(yt);*/

        dataAdapter = new MyCustomAdapter(this,
                R.layout.site_listitem, new ArrayList<Site>(SiteList));
        listView = (ListView) findViewById(R.id.blocked_sites);
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Site Site = (Site) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        Site.getName()+" is currently "+Site.getCode(),
                        Toast.LENGTH_LONG).show();
            }
        });

        Context cont = this;

        Button blButt = (Button) findViewById(R.id.block_butt);
        blButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText et_site = (EditText) findViewById(R.id.edit_site_name);
                String sitename = et_site.getText().toString();
                if(site_qualifies(sitename)) {
                    et_site.setText("");
                    Site site = blockSite(sitename);
                    SiteList.add(site);
                    dataAdapter = new MyCustomAdapter(cont,
                            R.layout.site_listitem, new ArrayList<Site>(SiteList));
                    listView = (ListView) findViewById(R.id.blocked_sites);
                    listView.setAdapter(dataAdapter);
                    StringBuffer responseText = new StringBuffer();
                    responseText.append(sitename + " was " + site.getCode().toLowerCase(Locale.ROOT) + "\n");


                    Toast.makeText(getApplicationContext(),
                            responseText, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Invalid Site Name", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button unbButt = (Button) findViewById(R.id.unblock_butt);
        unbButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following sites were unblocked :\n");

                //ArrayList<Site> SiteList = dataAdapter.SiteList;
                boolean flag = true;
                ArrayList<Site> itt = new ArrayList<>(SiteList);
                for(Site site : itt){
                    if(site.isSelected()){
                        flag = false;
                        unblockSite(site);
                        SiteList.remove(site);
                        dataAdapter = new MyCustomAdapter(cont,
                                R.layout.site_listitem, new ArrayList<Site>(SiteList));
                        listView = (ListView) findViewById(R.id.blocked_sites);
                        listView.setAdapter(dataAdapter);
                        responseText.append("\n" + site.getName());
                    }
                }
                if(flag) responseText = new StringBuffer("No sites were selected for unblocking");
                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<Site> {

        private ArrayList<Site> SiteList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Site> SiteList) {
            super(context, textViewResourceId, SiteList);
            this.SiteList = new ArrayList<Site>();
            this.SiteList.addAll(SiteList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.site_listitem, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.site_check);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Site Site = (Site) cb.getTag();
                        String status = (cb.isChecked() ? "Marked " : "Unmarked ") + cb.getText() + " for Unblocking";
                        Toast.makeText(getApplicationContext(),
                                status,
                                Toast.LENGTH_LONG).show();
                        Site.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Site Site = SiteList.get(position);
            holder.code.setText(" (" +  Site.getCode() + ")");
            holder.name.setText(Site.getName());
            holder.name.setChecked(Site.isSelected());
            holder.name.setTag(Site);

            return convertView;

        }

    }

    //TODO : Finish following functions

    private void unblockSite(Site site){

    }

    private Site blockSite(String name){
        Site site = new Site("BLOCKED",name,false);
        return site;
    }

    private boolean site_qualifies(String name)
    {
        if(name.equals("")) return false;
        return true;
    }
}