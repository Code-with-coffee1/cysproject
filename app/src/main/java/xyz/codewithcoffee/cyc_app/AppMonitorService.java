package xyz.codewithcoffee.cyc_app;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;

public class AppMonitorService extends AccessibilityService {

    private static final String TAG = MainActivity.TAG;
    private ArrayList<String> appList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Context cont = this;
        Log.d(TAG,"Sticky onStartCommand() Called");
        appList = (ArrayList<String>) intent.getSerializableExtra("applist");
        for(String app : appList)
        {
            Log.d(TAG,"Banned App : "+app);
        }
        return START_STICKY;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //Log.d(TAG,"Sticky onServiceConnected() Called");
        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"Sticky onAccessibilityEvent() Called");
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );

                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {
                    String curr = componentName.flattenToShortString();
                    Log.d(TAG, "CurrentActivity : " + curr);
                    if(appList==null)
                    {
                        Log.d(TAG,"NullPointer in applist");
                        return;
                    }
                    for(String app : appList) {
                        if(curr.contains(app)&&(!curr.contains("xyz.codewithcoffee.cyc_app")))
                        {
                            Log.d(TAG,"Match !");
                            Intent intent=new Intent(this, AppBlockingOverlay.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                            am.killBackgroundProcesses(app);
                        }
                    }
                }
            }
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        //Log.d(TAG,"Sticky tryGetActivity() Called");

        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {}

    //REDUNDANT
    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        return true;
    }

    public void usageAccessSettingsPage(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
