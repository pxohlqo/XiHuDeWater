package com.cracky_axe.pxohlqo.xihudewater.model;

import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.ResultBeanImageLinkObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class ObjectBoxUtils {
    public static final String TAG = "ObjectBoxUtils";

    public List<ResultBeanImageLinkObjectBox> getAllDataAsList(BoxStore boxStore) {
        Box<ResultBeanImageLinkObjectBox> box = boxStore.boxFor(ResultBeanImageLinkObjectBox.class);
        return box.getAll();
    }
}
