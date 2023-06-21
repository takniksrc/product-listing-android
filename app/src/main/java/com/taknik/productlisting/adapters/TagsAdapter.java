package com.taknik.productlisting.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taknik.productlisting.R;
import com.taknik.productlisting.activities.AddProductActivity;
import com.taknik.productlisting.database.SqLiteDatabase;
import com.taknik.productlisting.models.Product;
import com.taknik.productlisting.models.Tag;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder>{

    List<Tag> tagList;
    Context context;
    Activity activity;
    SqLiteDatabase mydb ;

    public TagsAdapter(List<Tag> tagList, Context context, Activity activity) {
        this.tagList = tagList;
        this.context = context;
        this.activity = activity;
        mydb = new SqLiteDatabase(activity);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tags_listview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.tagTV.setText(tagList.get(position).getTag());



        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mydb.deleteProduct(contentList.get(position).getId());
                tagList.remove(position);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }


    public String getTags() {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<tagList.size(); i++){
            stringBuilder.append(tagList.get(i).getTag());
            if (i<tagList.size()-1){
                stringBuilder.append(";");
            }
        }
        String tags = stringBuilder.toString();
        return tags;
    }

    public void addTag(List<Tag> tags) {
        this.tagList.clear();
        this.tagList = tags;
        notifyDataSetChanged();
    }
    public void addTag(Tag tag) {
        tagList.add(tag);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView tagTV;
        ImageView deleteBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tagTV =(TextView) itemView.findViewById(R.id.tagTV);
            this.deleteBtn =(ImageView) itemView.findViewById(R.id.deleteBtn);

        }

    }


}