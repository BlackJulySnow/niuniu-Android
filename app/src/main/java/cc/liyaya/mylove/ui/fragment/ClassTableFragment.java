package cc.liyaya.mylove.ui.fragment;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cc.liyaya.mylove.MyApplication;
import cc.liyaya.mylove.R;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.dao.ClassDao;
import cc.liyaya.mylove.dao.WeatherDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.FragmentClassTableBinding;
import cc.liyaya.mylove.model.Class;
import cc.liyaya.mylove.model.Weather;
import cc.liyaya.mylove.tool.DateUtil;
import cc.liyaya.mylove.tool.HttpUtil;
import cc.liyaya.mylove.tool.ResourceTool;
import cc.liyaya.mylove.tool.StringUtil;
import cc.liyaya.mylove.tool.WeatherTool;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClassTableFragment extends Fragment {
    private String TAG = "HomeFragment";
    private FragmentClassTableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMenu();

        long monday = DateUtil.getMonday();
        long today = DateUtil.getToday();
        WeatherDao dao = DatabaseUsage.getInstance(getContext()).weatherDao();
        ClassDao classDao = DatabaseUsage.getInstance(getContext()).classDao();
        for (int i = 0;i < 6;i++){
            long thisDay = monday + DateUtil.dayTime * i;
            Weather weather = dao.get(thisDay);
            if (weather != null)
                addWeather(i + 1,weather.getIcon(),weather.getMinTemp() + "-" + weather.getMaxTemp());
            else
                addWeather(i + 1, "100", "11-12");

            addWeekDaily(i + 1, MyConstant.WEEK[i + 1], new SimpleDateFormat("dd").format(new Date(thisDay)),today == thisDay);
            List<Class> classes = classDao.queryByDate(thisDay);
//
            if (classes != null)
                for (Class cls : classes){
                    addClass(MyConstant.CLASS_EMBEDDING[cls.getNum()], i + 1, StringUtil.append2(cls.getName()),cls.getPlace()+cls.getClassroom());
                }
        }

        return root;
    }

    private void addClass(int i, int j, String className,String toast) {
        CardView view = (CardView)View.inflate(getContext(), R.layout.item_class, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),toast,Toast.LENGTH_SHORT).show();
            }
        });
        GridLayout.Spec rowSpec = GridLayout.spec(i, 1.0f);
        GridLayout.Spec columnSpec = GridLayout.spec(j, 1.0f);
        ((TextView) view.findViewById(R.id.class_name)).setText(className);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        binding.homeGrid.addView(view, params);
    }

    private void addWeekDaily(int i, String week, String day,boolean color) {
        CardView view = (CardView) View.inflate(getContext(), R.layout.item_week_daily, null);
        if (color)
            view.setCardBackgroundColor(getResources().getColor(R.color.lightblue));
        GridLayout.Spec rowSpec = GridLayout.spec(0, 1.0f);
        GridLayout.Spec columnSpec = GridLayout.spec(i, 1.0f);
        ((TextView) view.findViewById(R.id.item_week)).setText(week);
        ((TextView) view.findViewById(R.id.item_day)).setText(day);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        binding.homeGrid.addView(view, params);
    }

    private void addWeather(int i, String icon, String text) {
        View view = View.inflate(getContext(), R.layout.item_weather, null);
        GridLayout.Spec rowSpec = GridLayout.spec(1, 1.0f);
        GridLayout.Spec columnSpec = GridLayout.spec(i, 1.0f);
        view.findViewById(R.id.item_day).setBackgroundResource(ResourceTool.getDrawableId("weather_" + icon));
        ((TextView) view.findViewById(R.id.item_temp)).setText(text);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        binding.homeGrid.addView(view, params);
    }
    public void initMenu(){
        getActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.class_table_menu,menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.push_test:
                        pushTest();
                        return true;
                }
                return false;
            }
        });
    }
    public void pushTest(){
        HashMap<String,String> map = new HashMap<>();
        map.put("token",MyConstant.PushToken);
        HttpUtil.post(MyConstant.PushTestURL, map, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Looper.prepare();
                Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}