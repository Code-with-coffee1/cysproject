package xyz.codewithcoffee.cyc_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatUI extends AppCompatActivity {

    private static final String TAG = "FB_MSG";
    private DatabaseReference fb_data = FirebaseDatabase.getInstance().getReference();

    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatui);
        displayChatMessages();
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                //fb_data.child("users").child("name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                //fb_data.child("users").child("msg").setValue(input.getText().toString());
                fb_data.child("chats")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );
                Log.d(TAG,"Sent Text : "+input.getText().toString()+ " from : "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                // Clear the input
                input.setText("");
                displayChatMessages();
            }
        });
        displayChatMessages();
    }

    private void displayChatMessages()
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("chats");
        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                /*.setLifecycleOwner(new LifecycleOwner() {
                    @NonNull
                    @Override
                    public Lifecycle getLifecycle() {
                        return null;
                    }
                })*/
                .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull ChatMessage model) {
                holder.setMessage_text(model.getMessageText());
                holder.setMessage_user(model.getMessageUser());
                holder.setMessage_time(model.getMessageTime());
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message, parent, false);

                return new ChatHolder(view);
            }
        };
        adapter.startListening();
        RecyclerView rv = findViewById(R.id.recycler_view);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
        rv.smoothScrollToPosition(adapter.getItemCount());
    }
}