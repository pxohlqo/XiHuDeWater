package com.cracky_axe.pxohlqo.xihudewater.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cracky_axe.pxohlqo.xihudewater.model.DataProvider;
import com.cracky_axe.pxohlqo.xihudewater.model.net.ResponseUtils;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidBean;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox;

import java.util.concurrent.Callable;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter {

    public static final String TAG = "HomePresenter";

    public static final String PREFERENCE_FILE = "XiHuDePreference";
    public static final String PREF_LOADED_PAGES = "loadedPages";

    private static BoxStore mBoxStore;
    private static Box<GankAndroidResultBeanObjectBox> mBox;
    private static HomeAdapter mAdapter;
    private static Context mContext;

    public HomePresenter(BoxStore boxStore, HomeAdapter homeAdapter, Context context) {
        mBoxStore = boxStore;
        mAdapter = homeAdapter;
        mContext = context;
        initBox();
    }

    private void initBox() {
        mBox = mBoxStore.boxFor(GankAndroidResultBeanObjectBox.class);
    }

    public HomeAdapter getAdapter() {
        return mAdapter;
    }

    public Box<GankAndroidResultBeanObjectBox> getmBox() {
        return mBox;
    }

    public void getAllEntitiesFromObjectBox() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(getAllEntitiesFromObjectBoxObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                mAdapter.swapData(mBox);
                mAdapter.notifyDataSetChanged();
            }
        }));
    }

    private static Observable<String> getAllEntitiesFromObjectBoxObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                DataProvider dataProvider = new DataProvider();
                dataProvider.getAllEnttitiesFromObjectBox(mBox);
                return Observable.just("get all entities from DB!");
            }
        });
    }

    public void requestDataFromApi() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(requestDataFromApiObservable(1)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: request error");
            }

            @Override
            public void onComplete() {
                mAdapter.swapData(mBox);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onComplete: request complete");
            }
        }));
    }

    public void requestMoreDataFromApi() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(requestDataFromApiObservable(getLoadedPage() + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        recordLoadedPages(getLoadedPage() +1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: request error");
                    }

                    @Override
                    public void onComplete() {
                        mAdapter.swapData(mBox);
                        mAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onComplete: request complete");
                    }
                }));
    }

    private static Observable<String> requestDataFromApiObservable(final int requestPage) {
        Log.d(TAG, "requestDataFromApiObservable: page" + requestPage);
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                ResponseUtils responseUtils = new ResponseUtils();
                String rawResponseJson = responseUtils.getAndroidResponseJsonBodyString(5, requestPage);
                //Log.d(TAG, "call: get request body");
                GankAndroidBean gankAndroidBean = responseUtils.convertJsonBodyToBean(rawResponseJson);
                //Log.d(TAG, "call: convert json");
                responseUtils.copyBeanToObjectBox(mBoxStore, gankAndroidBean);
                //Log.d(TAG, "call: copy to db");
                return Observable.just("request data from api!");
            }
        });
    }

    public void clearObjectBox() {
        mBox.removeAll();
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_FILE, 0);
        preferences.edit().putInt(PREF_LOADED_PAGES, 1).apply();
        Log.d(TAG, "clearObjectBox: " + mBox.count());
        mAdapter.swapData(mBox);
    }

    private void recordLoadedPages(int currentPage) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_FILE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_LOADED_PAGES, currentPage);
        Log.d(TAG, "recordLoadedPages: recorded page: " + currentPage);
        editor.apply();
    }

    private int getLoadedPage() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCE_FILE, 0);
        Log.d(TAG, "getLoadedPage: page: " +preferences.getInt(PREF_LOADED_PAGES, 1));
        return preferences.getInt(PREF_LOADED_PAGES, 1);
        }
}
