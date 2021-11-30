package xyz.codewithcoffee.cyc_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import xyz.codewithcoffee.cyc_app.Appitem;
import xyz.codewithcoffee.cyc_app.R;
import xyz.codewithcoffee.cyc_app.ViewHolder.AppViewHolder;

import java.util.List;

public class AppAdapters extends RecyclerView.Adapter<AppViewHolder> {
    public Context mContext;
    public List<Appitem> appitemList;

    public AppAdapters(Context mContext, List<Appitem> appitemList) {
        this.mContext = mContext;
        this.appitemList = appitemList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_apps,parent,false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        holder.name_of_app.setText(appitemList.get(position).getName());
        holder.icon.setImageDrawable(appitemList.get(position).getIcon());
        holder.lock_status.setImageResource(R.drawable.ic_baseline_lock_open_24);

    }

    @Override
    public int getItemCount() {
        return appitemList.size();
    }
}
