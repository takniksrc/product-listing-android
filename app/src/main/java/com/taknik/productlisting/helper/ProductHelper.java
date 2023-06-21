package com.taknik.productlisting.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.taknik.productlisting.activities.AddProductActivity;
import com.taknik.productlisting.activities.ProductActivity;
import com.taknik.productlisting.adapters.TagsAdapter;
import com.taknik.productlisting.database.SqLiteDatabase;
import com.taknik.productlisting.models.Product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProductHelper {

    static final int REQUEST_IMAGE_CAPTURE = 1, SELECT_IMAGE = 3;
    int CAMERA_PERMISSION_REQUEST_CODE = 0, STORAGE_PERMISSION_REQUEST_CODE=1, PERMISSION_REQUEST_CODE = 2;
    private static final int QR_CODE= 1122;

    SqLiteDatabase mydb;

    Activity activity;

    public ProductHelper(Activity activity) {
        this.activity = activity;
        mydb = new SqLiteDatabase(activity);
    }

    public byte[] bitmapToByyeArray(Bitmap imageBitmap){
        byte[] photoData = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        try {
            photoData = IOUtils.toByteArray(inputStream);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return photoData;
    }

    public boolean cameraPermission(){
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;

        }
        return true;
    }

    public boolean storagePermission(){
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void pickImageFromCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void pickImageFromGallery(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent.createChooser(intent,"Select Image"), SELECT_IMAGE );

    }

    public void svaeProduct(String brand, String title, String subTitle, String productCode, String productPrice, String scaleNo, byte[] photoData, String productMeasureUnit, String mode ,Product product, TagsAdapter tagsAdapter)
    {
        if (brand.isEmpty() || brand.length()>50 ){
            Toast.makeText(activity, "Brand length must be between 1 to 50 character's", Toast.LENGTH_SHORT).show();
        } else if (!title.isEmpty() && title.length()>20) {
            Toast.makeText(activity, "Title length must be less or equal than 20 character's", Toast.LENGTH_SHORT).show();
        } else if (!subTitle.isEmpty() && subTitle.length()>50) {
            Toast.makeText(activity, "Sub Title length must be less or equal than 50 character's", Toast.LENGTH_SHORT).show();
        } else if (!productCode.isEmpty() && productCode.length()>15) {
            Toast.makeText(activity, "Product Code length must be less or equal than 15 character's", Toast.LENGTH_SHORT).show();
        }else if (!productPrice.isEmpty() && Double.parseDouble(productPrice)>999999.99) {
            Toast.makeText(activity, "Product Price length must be less or equal than 999999.99", Toast.LENGTH_SHORT).show();
        }else {

            if (product == null){
                product = new Product();
            }

            String tags = tagsAdapter.getTags();

            product.setBrand(brand);
            product.setTitle(title);
            product.setSubTitle(subTitle);
            product.setProductCode(productCode);
            product.setProductPrice(productPrice);
            product.setScaleNo(scaleNo);
            product.setTag(tags);
            product.setProductMeasureUnit(productMeasureUnit);

            if (mydb.isProductExist(product.getId(), brand, title)){
                Toast.makeText(activity, "Product with same brand and title already exist", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mode.equals("add")){
                if (photoData == null){
                    Toast.makeText(activity, "Product image required", Toast.LENGTH_SHORT).show();
                } else {
                    product.setProductImage(photoData);
                    long a = mydb.saveProduct(product);
                    if (a > 0) {
                        Intent intent = new Intent(activity, ProductActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Toast.makeText(activity, "error while saving data", Toast.LENGTH_SHORT).show();
                    }
                }

            }else {
                if (photoData != null ){
                    product.setProductImage(photoData);
                }

                mydb.updateProduct(product);
                Intent intent = new Intent(activity, ProductActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

        }

    }

    public void optionMenu() {

        final CharSequence[] items = { "Gallery", "Camera"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select your Preferences");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    dialog.dismiss();

                    if (!cameraPermission()){
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.CAMERA},
                                CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        pickImageFromCamera();
                    }

                } else if (items[item].equals("Gallery")) {
                    dialog.dismiss();

                    if (!storagePermission()){
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    } else {
                        pickImageFromGallery();
                    }

                }
            }
        });
        builder.show();
    }


}
