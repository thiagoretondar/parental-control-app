package fei.tcc.parentalcontrol.service;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import static fei.tcc.parentalcontrol.component.ForegroundProcessManager.getForegroundApp;

public class AppUsageInfoService extends IntentService {

    private static final String TAG = "AppUsageInfoService";

    private static final Integer DELAY_SCREEN_UNLOCKED = 2000; // 2 seconds in ms

    private static final Integer DELAY_SCREEN_LOCKED = 15000; // 15 seconds in ms

    private UsageStatsManager usageStatsManager;

    public AppUsageInfoService(String name) {
        super(name);
    }

    public AppUsageInfoService() {
        this(AppUsageInfoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        usageStatsManager = ((UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // only get information about the foreground app when it is with phone unlocked
                if (!isScreenLocked()) {
                    Log.d(TAG, "Phone is unlocked");
                    String foregroundApp = getForegroundApp();
                    // must save info in database
                    handler.postDelayed(this, DELAY_SCREEN_UNLOCKED);
                } else {
                    Log.d(TAG, "Phone is locked");
                    handler.postDelayed(this, DELAY_SCREEN_LOCKED);
                }

            }
        }, DELAY_SCREEN_UNLOCKED);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Verify if the phone is locked or not
     *
     * @return boolean
     */
    private boolean isScreenLocked() {
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }

}
