package com.android.myapplication.data;

import com.android.myapplication.model.AllUserModel;

public class DataHolder {
    private static final DataHolder instance = new DataHolder();

    public static DataHolder getInstance() {
        return instance;
    }

    private AllUserModel data;
    public AllUserModel getData() {
        return data;
    }

    public void setData(AllUserModel data) {
        this.data = data;
    }
}
