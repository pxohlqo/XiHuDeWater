package com.cracky_axe.pxohlqo.xihudewater.model;

import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox;

import java.util.List;

import io.objectbox.Box;

public class DataProvider {
    private static final String TAG = "DataProvider";

    public List<GankAndroidResultBeanObjectBox> getAllEnttitiesFromObjectBox(Box<GankAndroidResultBeanObjectBox> box) {
        return box.getAll();
    }

    public void clearObjectBox(Box<GankAndroidResultBeanObjectBox> box) {
        box.removeAll();
    }
}
