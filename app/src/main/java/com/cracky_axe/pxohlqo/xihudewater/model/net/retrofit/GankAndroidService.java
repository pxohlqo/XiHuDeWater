package com.cracky_axe.pxohlqo.xihudewater.model.net.retrofit;

import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankAndroidService {
    @GET("data/Android/{requestNum}/{requestPage}")
    Call<GankAndroidBean> gankAndroidBean(@Path("requestNum") int requestNum, @Path("requestPage") int requestPage);
}
