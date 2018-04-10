package com.cracky_axe.pxohlqo.xihudewater.model.net;

import com.cracky_axe.pxohlqo.xihudewater.model.net.retrofit.GankAndroidRawService;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidBean;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox_;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.ResultBeanImageLinkObjectBox;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ResponseUtils {

    private static final String TAG = "ResponseUtils";

    public static final String BAD_RESPONSE_FLAG = "Bad Response!";

    public static final String baseUrl = "http://gank.io/";
    public static final String QUERY_ANDROID = "Android";

    public ResponseUtils() {

    }

    public String getAndroidResponseJsonBodyString(int requestNum, int requestPages) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GankAndroidRawService service = retrofit.create(GankAndroidRawService.class);

        Call<ResponseBody> call = service.getGankAndroidRaw(requestNum, requestPages);

        try {
            String responseString = call.execute().body().string();
            //Log.d(TAG, "getAndroidResponseJsonBodyString: " + responseString);
            return responseString;
        } catch (IOException e) {
            e.printStackTrace();
            return BAD_RESPONSE_FLAG;
        }
    }

    public GankAndroidBean convertJsonBodyToBean(String rawJsonString) {

        GankAndroidBean bean = null;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<GankAndroidBean> gankAndroidBeanJsonAdapter = moshi.adapter(GankAndroidBean.class);

        try {
            bean = gankAndroidBeanJsonAdapter.fromJson(rawJsonString);
            //Log.d(TAG, "convertJsonBodyToBean: convert complete");
        } catch (IOException e) {
            e.printStackTrace();
            //Log.d(TAG, "convertJsonBodyToBean: " + rawJsonString);
        }

        return bean;
    }

    public void copyBeanToObjectBox(BoxStore boxStore, GankAndroidBean gankAndroidBean) {
        Box<GankAndroidResultBeanObjectBox> beanObjectBoxBox = boxStore.boxFor(GankAndroidResultBeanObjectBox.class);
        List<GankAndroidBean.ResultsBean> resultsBeans = gankAndroidBean.getResults();
        List<GankAndroidResultBeanObjectBox> beanObjectBoxes = new ArrayList<>();
        for (GankAndroidBean.ResultsBean resultsBean :
                resultsBeans) {
            List<GankAndroidResultBeanObjectBox> l = beanObjectBoxBox.query()
                    .equal(GankAndroidResultBeanObjectBox_.desc, resultsBean.getDesc())
                    .build().find();
            if (l.isEmpty()) {
                GankAndroidResultBeanObjectBox b = convertBeanToBox(resultsBean);
                beanObjectBoxes.add(b);
            } else {
                return;
            }
            //Log.d(TAG, "copyBeanToObjectBox: add " + resultsBean.get_id() + " complete");
        }
        //Log.d(TAG, "copyBeanToObjectBox: add to list");
        beanObjectBoxBox.put(beanObjectBoxes);
    }

    public GankAndroidResultBeanObjectBox convertBeanToBox(GankAndroidBean.ResultsBean resultsBean) {
        GankAndroidResultBeanObjectBox box = new GankAndroidResultBeanObjectBox();
        box.set_id(resultsBean.get_id());
        //Log.d(TAG, "convertBeanToBox: copy _id");
        box.setCreatedAt(resultsBean.getCreatedAt());
        box.setDesc(resultsBean.getDesc());
        //Log.d(TAG, "convertBeanToBox: copy description");
        //Log.d(TAG, "convertBeanToBox: " + resultsBean.images.toString());
        if (resultsBean.getImages().size() != 0) {
            //Log.d(TAG, "convertBeanToBox: image is not empty");
            copyImageLinksToBox(resultsBean, box);
        } else {
            //Log.d(TAG, "convertBeanToBox: image is empty");
            box.setImages(null);
        }
        //Log.d(TAG, "convertBeanToBox: copy image link");
        box.setPublishedAt(resultsBean.getPublishedAt());
        box.setSource(resultsBean.getSource());
        box.setType(resultsBean.getType());
        box.setUrl(resultsBean.getUrl());
        box.setUsed(resultsBean.isUsed());
        box.setWho(resultsBean.getWho());
        return box;
    }

    private void copyImageLinksToBox(GankAndroidBean.ResultsBean resultsBean, GankAndroidResultBeanObjectBox box) {
        ResultBeanImageLinkObjectBox imageLinkObjectBox = new ResultBeanImageLinkObjectBox();
        for (String imageUrl :
                resultsBean.getImages()) {
            //Log.d(TAG, "copyImageLinksToBox: get image link");
            imageLinkObjectBox.setImageUrl(imageUrl);
            box.images.add(imageLinkObjectBox);
        }
    }
}
