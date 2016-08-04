package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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

import fei.tcc.parentalcontrol.AppUsageInfoService;
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

        mUsageStatsManager = (UsageStatsManager) this.getSystemService("usagestats"); //Context.USAGE_STATS_SERVICE

        mOpenUsageSettingButton = (Button) findViewById(R.id.button_open_usage_setting);

        setContentView(R.layout.activity_app_usage);

        UsageStats appUsageStatistics = getUsageStatistics(UsageStatsManager.INTERVAL_YEARLY, "com.android.mms");

        Log.i(TAG, appUsageStatistics.getPackageName());
        Log.i(TAG, "FirstTimeStamp: " + String.valueOf(appUsageStatistics.getFirstTimeStamp()));
        Log.i(TAG, "LastTimeUsed: " + String.valueOf(appUsageStatistics.getLastTimeUsed()));
        Log.i(TAG, "TotalTimeInForeground: " + String.valueOf(appUsageStatistics.getTotalTimeInForeground()));
        Log.i(TAG, "LastTimeUsed - format: " + DateUtils.formatSameDayTime(appUsageStatistics.getLastTimeUsed(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));

        Toast.makeText(AppUsageActivity.this, "Voltando Activity!", Toast.LENGTH_SHORT).show();
    }

    public UsageStats getUsageStatistics(int intervalType, final String packageName) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the acscess to apps usage. ");
            Toast.makeText(this,
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }

        for (UsageStats usageStats : queryUsageStats) {
            if (usageStats.getPackageName().equalsIgnoreCase(packageName)) {
                return usageStats;
            }
        }

        // TODO how to do this in better way, it is ugly :/
        return null;
    }

}
