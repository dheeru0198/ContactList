package com.contactlist;

import android.graphics.Bitmap;

public class RowItem {
	private Bitmap image;
    private String name;
    private String number;
    
    public RowItem()
    {
    }
 
    public RowItem(Bitmap image,String name, String number) {
    	this.image = image;
        this.name = name;
        this.number = number;
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name + "\n" + number;
    }
}
