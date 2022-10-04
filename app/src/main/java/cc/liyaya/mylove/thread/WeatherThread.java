package cc.liyaya.mylove.thread;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import java.text.ParseException;

import cc.liyaya.mylove.MyApplication;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.model.Weather;
import cc.liyaya.mylove.tool.DateUtil;
import cc.liyaya.mylove.tool.WeatherTool;

public class WeatherThread implements Runnable{
    private String TAG = "WeatherThread";
    private Activity activity;

    public WeatherThread(Activity activity) {
        this.activity = activity;
    }

    private void getWeather7D(Location location) {
        String ll = WeatherTool.Location2String(location);
        QWeather.getWeather7D(MyApplication.getContext(), ll, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherDailyBean));
                for (WeatherDailyBean.DailyBean dailyBean : weatherDailyBean.getDaily()) {
                    Weather weather = new Weather();
                    weather.setIcon(dailyBean.getIconDay());
                    weather.setMaxTemp(dailyBean.getTempMax());
                    weather.setMinTemp(dailyBean.getTempMin());
                    weather.setDateText(dailyBean.getFxDate());
                    try {
                        weather.setDate(DateUtil.string2Date(dailyBean.getFxDate()).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, String.valueOf(DatabaseUsage.getInstance(MyApplication.getContext()).weatherDao().insert(weather)));
                }
            }
        });
    }

    private void getWeather(Location location) {
        String ll = WeatherTool.Location2String(location);
        QWeather.getWeatherNow(MyApplication.getContext(), ll, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因

                if (Code.OK == weatherBean.getCode()) {
                    WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    @Override
    public void run() {
        Location location = WeatherTool.getLocation(activity);
        getWeather7D(location);
    }
}
