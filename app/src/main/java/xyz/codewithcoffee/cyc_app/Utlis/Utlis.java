package xyz.codewithcoffee.cyc_app.Utlis;

import static android.app.AppOpsManager.MODE_ALLOWED;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import androidx.annotation.RequiresApi;

public class Utlis {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkPermission(Context ctx){
        AppOpsManager appOpsManager=(AppOpsManager)ctx.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),ctx.getPackageName());

        return mode == MODE_ALLOWED;
    }
}
