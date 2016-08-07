package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import fei.tcc.parentalcontrol.service.AppUsageInfoService;
import fei.tcc.parentalcontrol.R;

public class AppUsageActivity extends AppCompatActivity {

    private static final String TAG = ListAppsActivity.class.getSimpleName();

    private UsageStatsManager mUsageStatsManager;

    private Button mOpenUsageSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AppUsageInfoService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        mOpenUsageSettingButton = (Button) findViewById(R.id.button_open_usage_setting);

        setContentView(R.layout.activity_app_usage);
    }
}
