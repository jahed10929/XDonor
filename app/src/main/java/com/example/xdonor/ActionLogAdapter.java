package com.example.xdonor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionLogAdapter extends RecyclerView.Adapter<ActionLogAdapter.ViewHolder> {
    ArrayList<ActionLogModel> actionLogModelArrayList;

    public ActionLogAdapter(ArrayList<ActionLogModel> actionLogModelArrayList) {
        this.actionLogModelArrayList = actionLogModelArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_activity_log_view, parent, false);
        return new ActionLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.date.setText(actionLogModelArrayList.get(position).getDate());
        holder.time.setText(actionLogModelArrayList.get(position).getTime());
        holder.action.setText(actionLogModelArrayList.get(position).getAction());
    }

    @Override
    public int getItemCount() {
        return actionLogModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, action;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
            action = itemView.findViewById(R.id.tvAction);
        }
    }
}
