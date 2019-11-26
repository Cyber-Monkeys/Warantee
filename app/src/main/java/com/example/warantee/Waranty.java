package com.example.warantee;

public class Waranty {

    String id;
    String uid;
    String date;
    float amount;
    String category;
    int warantyPeriod;
    String sellerName;
    String sellerPhone;
    String sellerEmail;



    String imageLocation;
    public Waranty() {
        this.id = "x";
        this.uid = "xx";
        this.date = "xx/xx/xxxx";
        this.amount = -1;
        this.category = "2";
        this.warantyPeriod = -1;
        this.sellerName = "xxxx";
        this.sellerPhone = "xxxx";
        this.sellerEmail = "xx@xx.com";
        this.imageLocation = "";
    }
    public Waranty(String uid, String id, String date, float amount, String category, int warantyPeriod, String sellerName, String sellerPhone, String sellerEmail) {
        this.uid = uid;
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.warantyPeriod = warantyPeriod;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sellerEmail = sellerEmail;
        this.imageLocation = "";
    }


    public String getUid() { return uid; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getWarantyPeriod() {
        return warantyPeriod;
    }

    public void setWarantyPeriod(int warantyPeriod) {
        this.warantyPeriod = warantyPeriod;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getImageLocation() { return imageLocation; }

    public void setImageLocation(String imageLocation) { this.imageLocation = imageLocation; }
}
