package com.cracky_axe.pxohlqo.xihudewater;

import android.app.Application;

import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.MyObjectBox;

import io.objectbox.BoxStore;

public class App extends Application {
    public static final boolean EXTERNAL_DIR = false;

    private BoxStore boxStore;

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
