package xyz.codewithcoffee.cyc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionSet extends AppCompatActivity {

    int len;
    int no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set_land);
        Button justGo = (Button) findViewById(R.id.just_go);
        EditText noQues = (EditText) findViewById(R.id.no_ques);
        justGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                len = Integer.parseInt(noQues.getText().toString());
                no = 0;
                setContentView(R.layout.activity_question_set);
                ((EditText) findViewById(R.id.question_name)).setHint("Question No "+(++no)+" Title :");
                Button submit = (Button) findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(no>len)
                        {
                            no = 0;
                            len = -1;
                            setContentView(R.layout.activity_question_set_land);
                            ((TextView)findViewById(R.id.land_text)).setText("Thanks for submitting question set");
                            return;
                        }
                        String question,option_a,option_b,option_c,option_d;
                        int correct;
                        EditText qv,oa,ob,oc,od,cr;
                        qv = (EditText) findViewById(R.id.question_name);
                        oa = (EditText) findViewById(R.id.option_a_edit);
                        ob = (EditText) findViewById(R.id.option_b_edit);
                        oc = (EditText) findViewById(R.id.option_c_edit);
                        od = (EditText) findViewById(R.id.option_d_edit);
                        cr = (EditText) findViewById(R.id.option_correct);
                        question = qv.getText().toString();
                        option_a = oa.getText().toString();
                        option_b = ob.getText().toString();
                        option_c = oc.getText().toString();
                        option_d = od.getText().toString();
                        correct = Integer.parseInt(cr.getText().toString());
                        Question ques = new Question(question,no,option_a,option_b,option_c,option_d,correct);
                        qv.setText("");
                        oa.setText("");
                        ob.setText("");
                        oc.setText("");
                        od.setText("");
                        cr.setText("");
                        FirebaseDatabase.getInstance().getReference().child("exam").child("questions").
                                child("q"+no).setValue(ques);
                        qv.setHint("Question No "+(++no)+" Title :");
                        if(no>len)
                        {
                            no = 0;
                            len = -1;
                            setContentView(R.layout.activity_question_set_land);
                            ((TextView)findViewById(R.id.land_text)).setText("Thanks for submitting question set");
                            return;
                        }
                    }
                });
            }
        });
    }
}