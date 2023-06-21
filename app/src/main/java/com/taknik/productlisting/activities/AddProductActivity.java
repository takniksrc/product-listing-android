package com.taknik.productlisting.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.taknik.productlisting.R;
import com.taknik.productlisting.adapters.TagsAdapter;
import com.taknik.productlisting.database.SqLiteDatabase;
import com.taknik.productlisting.helper.ProductHelper;
import com.taknik.productlisting.models.Product;
import com.taknik.productlisting.models.Tag;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    TextInputEditText brandET, titleET, subTitleET, productCodeET, productPriceET, scaleNoET, tagET;
    ImageView productImageView, backBtn;
    AppCompatButton saveBtn;
    AutoCompleteTextView measureUnitTV;


    ImageView addTagBtn, qrBtn;
    RecyclerView tagRecyclerView;
    TagsAdapter tagAdapter;
    List<Tag> tagList = new ArrayList<>();


    String mode = "add";
    Product product = null;
    SqLiteDatabase mydb;


    static final int REQUEST_IMAGE_CAPTURE = 1, SELECT_IMAGE = 3;
    int CAMERA_PERMISSION_REQUEST_CODE = 0, STORAGE_PERMISSION_REQUEST_CODE=1, PERMISSION_REQUEST_CODE = 2;
    private static final int QR_CODE= 1122;
    byte[] photoData = null;

    ProductHelper productHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        widgetInitialization();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = tagET.getText().toString().trim();

                if (tag != null && !tag.isEmpty() && !tag.equals("") && tag.length()<31){
                    tagAdapter.addTag(new Tag(tag));
                    tagET.setText("");
                } else{
                    Toast.makeText(AddProductActivity.this, "Tag length must be less or equal than 20 character's", Toast.LENGTH_SHORT).show();
                }
            }
        });

        qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddProductActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(AddProductActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE);

                }else{
                    Intent intent = new Intent(AddProductActivity.this, QRActivity.class);
                    startActivityForResult(intent,QR_CODE);
                }


            }
        });

        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productHelper.optionMenu();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brand = brandET.getText().toString().trim();
                String title = titleET.getText().toString().trim();
                String subTitle = subTitleET.getText().toString().trim();
                String productCode = productCodeET.getText().toString().trim();
                String productPrice = productPriceET.getText().toString().trim();
                String scaleNo = scaleNoET.getText().toString().trim();
                String productMeasureUnit = measureUnitTV.getText().toString();

                //Toast.makeText(AddProductActivity.this, u, Toast.LENGTH_SHORT).show();
                productHelper.svaeProduct(brand, title, subTitle, productCode, productPrice, scaleNo, photoData, productMeasureUnit, mode, product, tagAdapter);

            }
        });

    }


    public void widgetInitialization(){

        brandET = findViewById(R.id.brandET);
        titleET = findViewById(R.id.titleET);
        subTitleET = findViewById(R.id.subTitleET);
        productCodeET = findViewById(R.id.productCodeET);
        productPriceET = findViewById(R.id.productPriceET);
        scaleNoET = findViewById(R.id.scaleNoET);
        tagET = findViewById(R.id.tagET);


        productImageView = findViewById(R.id.productImageView);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);
        addTagBtn = findViewById(R.id.addTagBtn);
        qrBtn = findViewById(R.id.qrCodeBtn);
        tagRecyclerView = findViewById(R.id.tagRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagRecyclerView.setLayoutManager(layoutManager);

        tagAdapter = new TagsAdapter((ArrayList<Tag>) tagList, getApplicationContext(), AddProductActivity.this );
        tagRecyclerView.setAdapter(tagAdapter);

        measureUnitTV = findViewById(R.id.measureUnitTV);
        String[] units = new String[]{"kg", "lbs", "g", "oz", "pcs"};

// Create the ArrayAdapter with a custom Filter
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(AddProductActivity.this, android.R.layout.simple_dropdown_item_1line, units) {
            @Override
            public android.widget.Filter getFilter() {
                return new android.widget.Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = units;
                        results.count = units.length;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged();
                    }
                };
            }
        };

        // Set the adapter to your AutoCompleteTextView
        measureUnitTV.setAdapter(unitsAdapter);


        mydb = new SqLiteDatabase(AddProductActivity.this);
        productHelper = new ProductHelper(AddProductActivity.this);

        try {
            mode =  getIntent().getStringExtra("mode");
            if (mode.equals("edit")){
                //product = (Product) getIntent().getSerializableExtra("product");
                String productId =  getIntent().getStringExtra("productId");
                product = mydb.getProduct(productId);
            }
        }catch (Exception e){

        }

        if (mode.equals("edit") && product != null){
            brandET.setText(product.getBrand());
            titleET.setText(product.getTitle());
            subTitleET.setText(product.getSubTitle());
            productCodeET.setText(product.getProductCode());
            productPriceET.setText(product.getProductPrice());
            scaleNoET.setText(product.getScaleNo());
            //measureUnitTV.setText(product.getProductMeasureUnit());

            String preselectedItem = product.getProductMeasureUnit();
            // Find the index of the preselected item
            int position = -1;
            for (int i = 0; i < units.length; i++) {
                if (units[i].equals(preselectedItem)) {
                    position = i;
                    break;
                }
            }

            // Set the preselected item
            if (position != -1) {
                measureUnitTV.setText(unitsAdapter.getItem(position));
                measureUnitTV.setSelection(unitsAdapter.getItem(position).length());
            }

            if (product.getTag() != null && !product.getTag().isEmpty() && !product.getTag().equals("")){
                String input = product.getTag();
                String[] array = input.split(";");

                for (String element : array) {
                    tagAdapter.addTag(new Tag(element));
                }
            }




            try {
                if (product.getProductImage() != null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(product.getProductImage() , 0, product.getProductImage() .length);
                    productImageView.setImageBitmap(bitmap);
                } else {
                    productImageView.setImageDrawable(getDrawable(R.drawable.placeholder));
                }

            }catch (Exception e){
                productImageView.setImageDrawable(getDrawable(R.drawable.placeholder));
            }


        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                productHelper.pickImageFromCamera();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                productHelper.pickImageFromGallery();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(AddProductActivity.this, QRActivity.class);
                startActivityForResult(intent,QR_CODE);            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            productImageView.setImageBitmap(imageBitmap);

            photoData = productHelper.bitmapToByyeArray(imageBitmap);

        }
        else if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK&& data != null && data.getData() != null) {

            // Get the Uri of data
            Uri filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap imageBitmap= MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                productImageView.setImageBitmap(imageBitmap);

                photoData = productHelper.bitmapToByyeArray(imageBitmap);

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }


        }
        else if (requestCode == QR_CODE) {
            if(resultCode == Activity.RESULT_OK){

                try {
                    String productCode = data.getStringExtra("productCode");
                    if (productCode!=null && !productCode.isEmpty() && !productCode.equals("")){
                        productCodeET.setText(productCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}