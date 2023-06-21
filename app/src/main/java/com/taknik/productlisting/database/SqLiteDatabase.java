package com.taknik.productlisting.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

import com.taknik.productlisting.models.Product;


public class SqLiteDatabase extends SQLiteOpenHelper {

    SQLiteDatabase db;
    //DataBase Name
    private static final String DATABASE_NAME="PRODUCT";

    //DataBase Version
    private static final int DATABASE_VERSION= 1;

    //DataBase Table Name
    private static final String PRODUCT_TABLE="PRODUCT_TABLE";

    //DataBase Table columns
    private static final String ID="ID";
    private static final String BRAND="BRAND"; //requried, max 50
    private static final String TITLE="TITLE"; //nullable, max 20
    private static final String SUB_TITLE="SUB_TITLE"; //nullable, max 50
    private static final String PRODUCT_CODE="PRODUCT_CODE"; //nullable, max 15
    private static final String SCALE_NO="SCALE_NO"; //nullable
    private static final String TAG="TAG"; //nullable, max 30
    private static final String PRODUCT_PRICE="PRODUCT_PRICE";  //nullable, 'between:0,999999.99'
    private static final String PRODUCT_IMAGE="PRODUCT_IMAGE";  // requried
    private static final String PRODUCT_MEASURE_UNIT="PRODUCT_MEASURE_UNIT"; //nullable, max 10


    Context context;

    public SqLiteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        //Toast.makeText(context,"DB Creater",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OCR_TABLE=" CREATE TABLE " +PRODUCT_TABLE+ "(" +ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "+ BRAND+ " TEXT, "+ TITLE+ " TEXT, "+ SUB_TITLE+ " TEXT, "+ PRODUCT_CODE+ " TEXT, "+ SCALE_NO+ " TEXT, "+ TAG+ " TEXT, "+ PRODUCT_PRICE+ " TEXT, "+ PRODUCT_MEASURE_UNIT+ " TEXT, "+PRODUCT_IMAGE+ " BLOB); ";
        db.execSQL(CREATE_OCR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PRODUCT_TABLE);
        onCreate(db);
    }

    public long saveProduct(Product product) {

        db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(BRAND,product.getBrand());
        cv.put(TITLE,product.getTitle());
        cv.put(SUB_TITLE,product.getSubTitle());
        cv.put(PRODUCT_CODE,product.getProductCode());
        cv.put(PRODUCT_PRICE,product.getProductPrice());
        cv.put(SCALE_NO,product.getScaleNo());
        cv.put(TAG,product.getTag());
        cv.put(PRODUCT_IMAGE,product.getProductImage());
        cv.put(PRODUCT_MEASURE_UNIT, product.getProductMeasureUnit());

        return db.insert(PRODUCT_TABLE, null, cv);

    }

    public List<Product> getAllProducts() {

        db=this.getReadableDatabase();
        String[] columns= new String[]{ID,BRAND,TITLE, SUB_TITLE, PRODUCT_CODE, PRODUCT_PRICE, SCALE_NO, TAG, PRODUCT_IMAGE, PRODUCT_MEASURE_UNIT};
        Cursor cursor=db.query(PRODUCT_TABLE,columns,null,null,null,null,null);

        List<Product> productList = new ArrayList<>();

        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {

                String id=cursor.getString(0);
                String brand=cursor.getString(1);
                String title=cursor.getString(2);
                String subTitle=cursor.getString(3);
                String productCode=cursor.getString(4);
                String productPrice=cursor.getString(5);
                String scaleNo=cursor.getString(6);
                String tag=cursor.getString(7);
                byte[] productImage=cursor.getBlob(8);
                String productMeasureUnit = cursor.getString(9);

                productList.add(new Product(id, brand, title, subTitle, productCode, productPrice, scaleNo, tag, productImage, productMeasureUnit));

            }
        }
        return productList;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BRAND,product.getBrand());
        cv.put(TITLE,product.getTitle());
        cv.put(SUB_TITLE,product.getSubTitle());
        cv.put(PRODUCT_CODE,product.getProductCode());
        cv.put(PRODUCT_PRICE,product.getProductPrice());
        cv.put(SCALE_NO,product.getScaleNo());
        cv.put(TAG,product.getTag());
        cv.put(PRODUCT_IMAGE,product.getProductImage());
        cv.put(PRODUCT_MEASURE_UNIT, product.getProductMeasureUnit());

        int a= db.update(PRODUCT_TABLE, cv, ID + "=" + product.getId(),null);
        db.close();
        return a;
    }

    public int deleteProduct( String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a=  db.delete(PRODUCT_TABLE, ID + "=" + id, null);
        db.close();
        return a;

    }

    public Product getProduct(String productId) {

        db=this.getReadableDatabase();
        String[] columns= new String[]{ID,BRAND,TITLE, SUB_TITLE, PRODUCT_CODE, PRODUCT_PRICE, SCALE_NO, TAG, PRODUCT_IMAGE, PRODUCT_MEASURE_UNIT};
        Cursor cursor=db.query(PRODUCT_TABLE,columns,null,null,null,null,null);

        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String id=cursor.getString(0);

                if (productId.equals(id)){

                    String brand=cursor.getString(1);
                    String title=cursor.getString(2);
                    String subTitle=cursor.getString(3);
                    String productCode=cursor.getString(4);
                    String productPrice=cursor.getString(5);
                    String scaleNo=cursor.getString(6);
                    String tag=cursor.getString(7);
                    byte[] productImage=cursor.getBlob(8);
                    String productMeasuereUnit = cursor.getString(9);

                    return new Product(id, brand, title, subTitle, productCode, productPrice, scaleNo, tag, productImage, productMeasuereUnit);
                }

            }
        }
        return null;
    }

    public boolean isProductExist(String id, String brand, String title) {

        db=this.getReadableDatabase();
        String[] columns= new String[]{ID,BRAND,TITLE};
        Cursor cursor=db.query(PRODUCT_TABLE,columns,null,null,null,null,null);


        if (cursor!=null)
        {
            while (cursor.moveToNext())
            {
                String _id=cursor.getString(0);
                String _brand=cursor.getString(1);
                String _title=cursor.getString(2);

                if (!_id.equals(id) && brand.equals(_brand) && title.equals(_title)){
                    return true;
                }

            }
        }
        return false;
    }


}