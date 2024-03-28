package com.mp012202200038_01.apppuma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder>{
    Context context;
    ArrayList<ListEvent> arrayList;
    OnItemClickListener onItemClickListener;

    public ListEventAdapter(Context context, ArrayList<ListEvent> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.subtitle.setText(arrayList.get(position).getContent());
        holder.day.setText(arrayList.get(position).getDay());
        holder.month.setText(arrayList.get(position).getMonth());
        holder.desc.setText(arrayList.get(position).getDesc());
        holder.time.setText(arrayList.get(position).getTime());
        holder.itemView.setOnClickListener(view -> onItemClickListener.onClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, day, month, desc, time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            subtitle = itemView.findViewById(R.id.event_location);
            day = itemView.findViewById(R.id.event_day);
            month = itemView.findViewById(R.id.event_month);
            desc = itemView.findViewById(R.id.event_description);
            time = itemView.findViewById(R.id.event_time);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(ListEvent listevent);
    }
}
