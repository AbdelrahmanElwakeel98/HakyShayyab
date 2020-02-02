package com.abdelrahman.irihackathon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdelrahman.irihackathon.Model.Answer;
import com.abdelrahman.irihackathon.Model.Manual;
import com.abdelrahman.irihackathon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Manual> manuals;

    public ManualAdapter(Context context, ArrayList<Manual> manuals){
        this.context = context;
        this.manuals = manuals;
    }

    @NonNull
    @Override
    public ManualAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_manual,parent,false);
        ManualAdapter.ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManualAdapter.ViewHolder holder, int position) {
        holder.title.setText(manuals.get(position).getTitle());
        holder.description.setText(manuals.get(position).getDescription());
        Picasso.get().load(manuals.get(position).getMedia()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return manuals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title, description;
        public ImageView img;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = itemView.findViewById(R.id.txt_headline);
            description = itemView.findViewById(R.id.txt_description);
            img = itemView.findViewById(R.id.img);

        }
    }
}
