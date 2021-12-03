package xyz.codewithcoffee.cyc_app.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.codewithcoffee.cyc_app.R;

public class AppViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon,lock_status;
    public TextView name_of_app;
    public AppViewHolder(@NonNull View itemView) {
        super(itemView);

        icon=itemView.findViewById(R.id.app_icon);
        lock_status=itemView.findViewById(R.id.lock_status);
        name_of_app=itemView.findViewById(R.id.app_name);
    }
}
