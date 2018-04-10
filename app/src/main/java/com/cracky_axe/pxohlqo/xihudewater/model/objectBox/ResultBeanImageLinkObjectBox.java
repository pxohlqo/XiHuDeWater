package com.cracky_axe.pxohlqo.xihudewater.model.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class ResultBeanImageLinkObjectBox {

    @Id
    public long objectBoxId;

    public ToOne<GankAndroidResultBeanObjectBox> gankAndroidResultBeanObjectBox;

    private String imageUrl;

    public ResultBeanImageLinkObjectBox() {
    }

    public long getObjectBoxId() {
        return objectBoxId;
    }

    public void setObjectBoxId(long objectBoxId) {
        this.objectBoxId = objectBoxId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ToOne<GankAndroidResultBeanObjectBox> getGankAndroidResultBeanObjectBox() {
        return gankAndroidResultBeanObjectBox;
    }

    public void setGankAndroidResultBeanObjectBox(ToOne<GankAndroidResultBeanObjectBox> gankAndroidResultBeanObjectBox) {
        this.gankAndroidResultBeanObjectBox = gankAndroidResultBeanObjectBox;
    }
}
