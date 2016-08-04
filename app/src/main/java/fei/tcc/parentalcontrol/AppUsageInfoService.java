package fei.tcc.parentalcontrol;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import fei.tcc.parentalcontrol.component.ProcessManager;

public class AppUsageInfoService extends IntentService {

    private static final String TAG = "MyService";

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
        Log.i(TAG, "FOREGROUND APP:" + ProcessManager.getForegroundApp());
    }
}
