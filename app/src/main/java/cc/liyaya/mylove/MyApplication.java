package cc.liyaya.mylove;

import android.app.Application;
import android.content.Context;

import com.qweather.sdk.view.HeConfig;

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        HeConfig.init("HE2110301116451245", "910cb2d6de04451093b5a0e723a442d2");
        HeConfig.switchToDevService();
    }
    //12312312
}
