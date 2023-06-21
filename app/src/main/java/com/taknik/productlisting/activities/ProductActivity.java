package com.taknik.productlisting.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.taknik.productlisting.R;
import com.taknik.productlisting.adapters.ProductAdapter;
import com.taknik.productlisting.database.SqLiteDatabase;
import com.taknik.productlisting.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    FloatingActionButton addBtn;
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    List<Product> contentList = new ArrayList<>();

    SqLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        addBtn = findViewById(R.id.addBtn);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        mydb = new SqLiteDatabase(ProductActivity.this);
        contentList = mydb.getAllProducts();
        productRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.VERTICAL, false));

        if (contentList!=null && !contentList.isEmpty()  && contentList.size()>0){
            Collections.reverse(contentList);
            productAdapter = new ProductAdapter((ArrayList<Product>) contentList, getApplicationContext(), ProductActivity.this );
            productRecyclerView.setAdapter(productAdapter);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
                finish();
            }
        });

    }
}