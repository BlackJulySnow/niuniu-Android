package cc.liyaya.mylove.ui.fragment;

import android.os.Bundle;
import android.os.Looper;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import cc.liyaya.mylove.R;
import cc.liyaya.mylove.adapter.DormAdapter;
import cc.liyaya.mylove.adapter.MemoAdapter;
import cc.liyaya.mylove.constant.MyConstant;
import cc.liyaya.mylove.dao.ClassDao;
import cc.liyaya.mylove.dao.WeatherDao;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.FragmentClassTableBinding;
import cc.liyaya.mylove.model.Class;
import cc.liyaya.mylove.model.Memo;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClassTableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initMenu();
        binding.classYear.setText(String.valueOf(DateUtil.getYear()));
        binding.classMonth.setText(String.format("%d",(DateUtil.getMonth())) + "月");
        long monday = DateUtil.getMonday();
        long today = DateUtil.getToday();
        if (today == monday + DateUtil.dayTime * 6) {//如果今天是周日，显示下周课表
            monday = monday + DateUtil.dayTime * 7;
        }
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
        initDorm();


        return root;
    }

    private void addClass(int i, int j, String className,String toast) {
        CardView view = (CardView)View.inflate(getContext(), R.layout.item_class, null);
        view.setOnClickListener(view1 -> Toast.makeText(getContext(),toast,Toast.LENGTH_SHORT).show());//显示上课地点
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
    public void initDorm(){
        RecyclerView recyclerView = binding.dormRecycle;
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        DormAdapter dormAdapter = new DormAdapter(DatabaseUsage.getInstance(getContext()).dormDao().queryAll());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(dormAdapter);
    }
}