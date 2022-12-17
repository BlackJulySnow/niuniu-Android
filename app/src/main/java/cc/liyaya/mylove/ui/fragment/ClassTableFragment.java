package cc.liyaya.mylove.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import cc.liyaya.mylove.R;
import cc.liyaya.mylove.adapter.DormAdapter;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.dao.ClassDao;
import cc.liyaya.mylove.dao.DormDao;
import cc.liyaya.mylove.dao.WeatherDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.FragmentClassTableBinding;
import cc.liyaya.mylove.model.Class;
import cc.liyaya.mylove.model.Weather;
import cc.liyaya.mylove.tool.DateUtil;
import cc.liyaya.mylove.tool.HttpUtil;
import cc.liyaya.mylove.tool.ResourceTool;
import cc.liyaya.mylove.tool.StringUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassTableFragment extends Fragment {
    private String TAG = "HomeFragment";
    private FragmentClassTableBinding binding;
    private WeatherDao weatherDao;
    private ClassDao classDao;
    private DormDao dormDao;
    private int minCount;
    private int year,month,day;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseUsage databaseUsage = DatabaseUsage.getInstance(getContext());
        weatherDao = databaseUsage.weatherDao();
        classDao = databaseUsage.classDao();
        dormDao = databaseUsage.dormDao();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        minCount = Integer.MAX_VALUE;
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        initMenu();
        initClassTable(DateUtil.getToday());
        initDorm();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        weatherDao = null;
        classDao = null;
        dormDao = null;
    }

    private void addClass(int i, int j, String className, String toast) {
        CardView view = (CardView)View.inflate(getContext(), R.layout.item_class, null);
//        view.setOnClickListener(view1 -> Toast.makeText(getContext(),toast,Toast.LENGTH_SHORT).show());//显示上课地点
        view.setOnClickListener(view1 -> Snackbar.make(this.getView(),toast,Snackbar.LENGTH_SHORT).setAction("懿懿知道啦", view2 -> {

        }).setActionTextColor(getResources().getColor(R.color.pink)).show());//显示上课地点
        GridLayout.Spec rowSpec = GridLayout.spec(i, 1.0f);
        GridLayout.Spec columnSpec = GridLayout.spec(j, 1.0f);
        ((TextView) view.findViewById(R.id.class_name)).setText(className);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        binding.homeGrid.addView(view, params);
        minCount = Math.min(binding.homeGrid.indexOfChild(view),minCount);

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
        minCount = Math.min(binding.homeGrid.indexOfChild(view),minCount);
    }

    private void addWeather(int i, String icon, String text) {
        View view = View.inflate(getContext(), R.layout.item_weather, null);
        GridLayout.Spec rowSpec = GridLayout.spec(1, 1.0f);
        GridLayout.Spec columnSpec = GridLayout.spec(i, 1.0f);
        view.findViewById(R.id.item_day).setBackgroundResource(ResourceTool.getDrawableId("weather_" + icon));
        ((TextView) view.findViewById(R.id.item_temp)).setText(text);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);

        binding.homeGrid.addView(view, params);
        minCount = Math.min(binding.homeGrid.indexOfChild(view),minCount);
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
                    case R.id.calendar:
                        new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                            while (binding.homeGrid.getChildCount() > minCount) {//删除所有添加的元素
                                binding.homeGrid.removeViewAt(minCount);
                            }
                            Log.e(TAG, String.valueOf(binding.homeGrid.getChildCount()));
                            Calendar c = Calendar.getInstance();
                            c.set(i,i1,i2,0,0,0);
                            year = i; month = i1; day = i2;
                            initClassTable(c.getTimeInMillis() / 1000 * 1000);
                        },year,month,day).show();
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
//                Looper.prepare();
                Snackbar.make(getView(),"Error!",Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Looper.prepare();
                Snackbar.make(getView(),"发送成功",Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        });
    }
    public void initClassTable(long start){
        binding.classYear.setText(String.valueOf(DateUtil.getYear(start)));
        binding.classMonth.setText(String.format("%d",(DateUtil.getMonth(start))) + "月");
        long monday = DateUtil.getMonday(start);
        if (start == monday + DateUtil.dayTime * 6) {//如果今天是周日，显示下周课表
            monday = monday + DateUtil.dayTime * 7;
        }

        for (int i = 0;i < 6;i++){
            long thisDay = monday + DateUtil.dayTime * i;
            Weather weather = weatherDao.get(thisDay);
            if (weather != null)
                addWeather(i + 1,weather.getIcon(),weather.getMinTemp() + "-" + weather.getMaxTemp());
            else
                addWeather(i + 1, "100", "查无记录");

            addWeekDaily(i + 1, MyConstant.WEEK[i + 1], new SimpleDateFormat("dd").format(new Date(thisDay)),start == thisDay);


            List<Class> classes = classDao.queryByDate(thisDay);
            if (classes != null)
                for (Class cls : classes){
                    addClass(MyConstant.CLASS_EMBEDDING[cls.getNum()], i + 1, StringUtil.append2(cls.getName()),cls.getPlace()+cls.getClassroom());
                }
        }
    }
    public void initDorm(){
        RecyclerView recyclerView = binding.dormRecycle;
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        DormAdapter dormAdapter = new DormAdapter(dormDao.queryAll());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(dormAdapter);
    }
}