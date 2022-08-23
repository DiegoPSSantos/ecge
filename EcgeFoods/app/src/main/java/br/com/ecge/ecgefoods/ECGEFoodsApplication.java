package br.com.ecge.ecgefoods;

import android.app.Application;
import android.util.Log;

public class ECGEFoodsApplication extends Application {

    private static final String TAG = "ECGEFoodsApplication";
    private static ECGEFoodsApplication instance = null;

    public static ECGEFoodsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ECGEFoodsApplication.onCreate()");
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "ECGEFoodsApplication.onTerminate()");
    }
}
