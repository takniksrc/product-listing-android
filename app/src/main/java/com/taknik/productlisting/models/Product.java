package com.taknik.productlisting.models;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Product implements Serializable {

    String brand;
    String title;
    String subTitle;
    String productCode;
    String productPrice;
    String scaleNo;
    String tag;
    byte[] productImage;
    String productMeasureUnit;

    String id;

    public Product() {
    }

    public Product(String id, String brand, String title, String subTitle, String productCode, String productPrice, String scaleNo, String tag, byte[] productImage, String productMeasureUnit) {
        this.brand = brand;
        this.title = title;
        this.subTitle = subTitle;
        this.productCode = productCode;
        this.productPrice = productPrice;
        this.scaleNo = scaleNo;
        this.tag = tag;
        this.productImage = productImage;
        this.id = id;
        this.productMeasureUnit = productMeasureUnit;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getProductMeasureUnit() {
        return productMeasureUnit;
    }

    public void setProductMeasureUnit(String productMeasureUnit) {
        this.productMeasureUnit = productMeasureUnit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getScaleNo() {
        return scaleNo;
    }

    public void setScaleNo(String scaleNo) {
        this.scaleNo = scaleNo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }
}
