package com.cracky_axe.pxohlqo.xihudewater.model.net.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GankAndroidRawService {
    @GET("api/data/Android/{requestNum}/{requestPage}")
    Call<ResponseBody> getGankAndroidRaw(@Path("requestNum") int requestNum, @Path("requestPage") int requestPage);
}
