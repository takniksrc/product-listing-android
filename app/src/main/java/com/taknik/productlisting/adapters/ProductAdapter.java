package com.taknik.productlisting.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.taknik.productlisting.R;
import com.taknik.productlisting.activities.AddProductActivity;
import com.taknik.productlisting.database.SqLiteDatabase;
import com.taknik.productlisting.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    List<Product> contentList;
    Context context;
    Activity activity;
    SqLiteDatabase mydb ;

    public ProductAdapter(List<Product> contentList, Context context, Activity activity) {
        this.contentList = contentList;
        this.context = context;
        this.activity = activity;
        mydb = new SqLiteDatabase(activity);


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_listview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.titleTV.setText("Title: "+ contentList.get(position).getTitle());
        holder.brandTV.setText("Brand: "+ contentList.get(position).getBrand());
        holder.tagTV.setText("Tags: "+ contentList.get(position).getTag());
        holder.productCodeTV.setText("Product Code: "+ contentList.get(position).getProductCode());

        try {

            if (contentList.get(position).getProductImage() != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(contentList.get(position).getProductImage() , 0, contentList.get(position).getProductImage() .length);
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageDrawable(activity.getDrawable(R.drawable.placeholder));
            }

        }catch (Exception e){
            holder.imageView.setImageDrawable(activity.getDrawable(R.drawable.placeholder));
        }


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb.deleteProduct(contentList.get(position).getId());
                contentList.remove(position);
//                if (contentList==null || contentList.isEmpty() || contentList.size()==0){
//                    noImageScreen.setVisibility(View.VISIBLE);
//                }
                notifyDataSetChanged();
            }
        });


        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, AddProductActivity.class);
                intent.putExtra("productId", String.valueOf(contentList.get(position).getId()));
                //intent.putExtra("product", contentList.get(position));
                intent.putExtra("mode", "edit");
                activity.startActivity(intent);
                activity.finish();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, AddProductActivity.class);
                intent.putExtra("productId", String.valueOf(contentList.get(position).getId()));
                //intent.putExtra("product", contentList.get(position));
                intent.putExtra("mode", "edit");
                activity.startActivity(intent);
                activity.finish();
            }
        });



    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView titleTV, brandTV, tagTV, productCodeTV;
        ImageView imageView;
        ImageView editBtn, deleteBtn;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleTV =(TextView) itemView.findViewById(R.id.titleTV);
            this.brandTV =(TextView) itemView.findViewById(R.id.brandTV);
            this.tagTV =(TextView) itemView.findViewById(R.id.tagTV);
            this.productCodeTV =(TextView) itemView.findViewById(R.id.productCodeTV);

            this.editBtn =(ImageView) itemView.findViewById(R.id.editBtn);
            this.deleteBtn =(ImageView) itemView.findViewById(R.id.deleteBtn);
            this.imageView =itemView.findViewById(R.id.image_view);
            this.relativeLayout =(RelativeLayout) itemView.findViewById(R.id.relativeLayout);


        }

    }


}