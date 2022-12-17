package cc.liyaya.mylove.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.hms.push.HmsMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import cc.liyaya.mylove.R;
import cc.liyaya.mylove.database.DatabaseUsage;
import cc.liyaya.mylove.databinding.ActivityMainBinding;
import cc.liyaya.mylove.thread.ClassThread;
import cc.liyaya.mylove.thread.DormThread;
import cc.liyaya.mylove.thread.MemoThread;
import cc.liyaya.mylove.thread.WeatherThread;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setAutoInitEnabled(true);//推送功能
        setSupportActionBar(binding.toolbarMain);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        new Thread(new ClassThread()).start();
        new Thread(new MemoThread()).start();
        new Thread(new DormThread()).start();
        new Thread(new WeatherThread(this)).start();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_class_table, R.id.navigation_dashboard, R.id.navigation_center)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (!isNotificationEnabled(this)) {
            openPush(MainActivity.this);
            Log.e(TAG, "NO");
        }
    }
    private void setAutoInitEnabled(final boolean isEnable) {
        // 设置自动初始化
        HmsMessaging.getInstance(this).setAutoInitEnabled(isEnable);
    }


    public static void openPush(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);
        activity.startActivity(intent);
    }
    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context.getApplicationContext()).areNotificationsEnabled();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        DatabaseUsage.destroy();
        super.onDestroy();
    }
}