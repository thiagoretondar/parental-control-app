package fei.tcc.parentalcontrol;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import fei.tcc.parentalcontrol.component.ProcessManager;

public class AppUsageInfoService extends IntentService {

    private static final String TAG = "MyService";

    private UsageStatsManager mUsageStatsManager;

    private ActivityManager mActivityManager;

    public AppUsageInfoService(String name) {
        super(name);
    }

    public AppUsageInfoService() {
        this(AppUsageInfoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Antes do RUN");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        Log.i(TAG, "START COMMAND");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //AppUsageInfoService.this.getUsageStatistics(UsageStatsManager.INTERVAL_DAILY, "com.android.mms");
                AppUsageInfoService.this.getForegroundApp();
                handler.postDelayed(this, 1000);
            }
        }, 2000);
        return super.onStartCommand(intent, flags, startId);
    }

    public void getForegroundApp() {
//        String topPackageName = "";
//        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
//        long time = System.currentTimeMillis();
//        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time - 60000*60*24, time+1000);
//        // We get usage stats for the last 10 seconds
//        Log.i(TAG, "INICIO: " + DateUtils.formatSameDayTime(time - 60000, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
//        Log.i(TAG, "INICIO: " + DateUtils.formatSameDayTime(time + 1000, System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
//        // Sort the stats by the last time used
//        if(stats != null) {
//            SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
//            for (UsageStats usageStats : stats) {
//                mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
//            }
//            if(mySortedMap != null && !mySortedMap.isEmpty()) {
//                topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//            }
//            Log.i(TAG, "NOME DO PACOTE: " + topPackageName);
//        }
//        return topPackageName;
        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("Execution time: ").append(System.currentTimeMillis() - startTime).append("ms\n");
        sb.append("Running apps:\n");
        List<ProcessManager.Process> processes = ProcessManager.getRunningApps();
        for (ProcessManager.Process process : processes) {
            sb.append('\n').append(process.name);
        }
        //new AlertDialog.Builder(MainActivity.this).setMessage(sb.toString()).show();
        //Log.i(TAG, sb.toString());
        Log.i(TAG, "FOREGROUND APP:" + ProcessManager.getForegroundApp());
    }

    public UsageStats getUsageStatistics(int intervalType, final String packageName) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        for (UsageStats usageStats : queryUsageStats) {
            if (usageStats.getPackageName().equalsIgnoreCase(packageName)) {
                Log.i(AppUsageInfoService.TAG, "LAST TIME USED" + String.valueOf(usageStats.getLastTimeUsed()));
                return usageStats;
            }
        }

        // TODO how to do this in better way, it is ugly :/
        return null;
    }
}
