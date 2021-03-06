package xyz.codewithcoffee.cyc_app;

import static xyz.codewithcoffee.cyc_app.MainActivity.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.StringTokenizer;

public class Timetable extends AppCompatActivity {

    Context context;
    TimePicker start,end;
    File rtFile;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        context = this;
        start = findViewById(R.id.time_start);
        end = findViewById(R.id.time_end);
        rtFile = new File(context.getFilesDir(),"restrict_time.txt");
        ((ImageButton)findViewById(R.id.submit_butt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tym start_tym = fetchRestrictStartTime(),end_tym =fetchRestrictEndTime();
                writeTextData(rtFile,start_tym.getHour()+" "
                        + start_tym.getMin()+" \n"
                        +end_tym.getHour()+" "
                        +end_tym.getMin()+" \n"
                );
                Toast.makeText(context,"Successfully set Restriction Time",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Timetable.this,Dashboard.class));
            }
        });
        Tym[] ttp = getTymTuple(rtFile);
        start.setHour(ttp[0].getHour());
        start.setMinute(ttp[0].getMin());
        end.setHour(ttp[1].getHour());
        end.setMinute(ttp[1].getMin());
    }

    private Tym[] getTymTuple(File myfile) {
        Tym[] val = new Tym[]{new Tym(-1,-1,""),new Tym(-1,-1,"")};
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(myfile);
            BufferedReader br = new BufferedReader(fileReader);
            StringTokenizer stoken = new StringTokenizer(br.readLine());
            val[0].setHour(Integer.parseInt(stoken.nextToken()));
            val[0].setMin(Integer.parseInt(stoken.nextToken()));
            stoken = new StringTokenizer(br.readLine());
            val[1].setHour(Integer.parseInt(stoken.nextToken()));
            val[1].setMin(Integer.parseInt(stoken.nextToken()));
            return val;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Tym getCurrTime()
    {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return new Tym(hour,min,"");
    }

    private Tym fetchRestrictStartTime()
    {
        int hour = start.getCurrentHour();
        int min = start.getCurrentMinute();
        return new Tym(hour,min,"");
    }

    private Tym fetchRestrictEndTime()
    {
        int hour = end.getCurrentHour();
        int min = end.getCurrentMinute();
        return new Tym(hour,min,"");
    }

    public static boolean banTime(Tym start, Tym end)
    {
        if(start==null||end==null) return false;
        if(end.getHour()<start.getHour())
        {
            end.setHour(end.getHour()+24);
        }
        Tym curr = getCurrTime();
        boolean after,before;
        after = before = false;

        if(curr.getHour()>start.getHour()) {after=true;}
        else if(curr.getHour()==start.getHour())
        {
            after = curr.getMin()>=start.getMin();
        }

        if(curr.getHour()<end.getHour()) {before=true;}
        else if(curr.getHour()==end.getHour())
        {
            before = curr.getMin()<=end.getMin();
        }

        return after && before;
    }

    private void writeTextData(File file, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            Log.d(TAG, "Wrote to " + file.getAbsolutePath().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}