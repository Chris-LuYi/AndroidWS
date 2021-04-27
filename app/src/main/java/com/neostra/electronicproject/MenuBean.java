package com.neostra.electronicproject;

import android.support.annotation.IdRes;

/**
 * @author njmsir
 * Created by njmsir on 2019/3/8.
 */
public class MenuBean {
    private String name;
    private double price;
    @IdRes
    private int resId;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
