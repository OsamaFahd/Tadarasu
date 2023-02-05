package com.example.tadarasu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class myadapter  extends RecyclerView.Adapter<myadapter.MyViewHolder> {
    Context context;
    ArrayList<Content> list;


    public myadapter(Context context, ArrayList<Content> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.content_rows,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Content content = list.get(position);
        holder.name.setText(content.getName());
        holder.title.setText(content.getTitle());
        Glide.with(holder.img.getContext()).load(content.getImage()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,SingleContent.class)
                        .putExtra("ContentID",content.getPostID())
                        .putExtra("ContentTitle",content.getTitle())
                        .putExtra("ContentCategory",content.getCategory())
                        .putExtra("ContentText",content.getText())
                        .putExtra("ContentSound",content.getSound())
                        .putExtra("ContentVideo",content.getVideo())
                        .putExtra("ContentName",content.getName()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name, title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.NameScholar);
            title = itemView.findViewById(R.id.ContentTitle);
            img = itemView.findViewById(R.id.imageSci);

        }
    }

}

