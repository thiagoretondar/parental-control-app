package fei.tcc.parentalcontrol.service;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import fei.tcc.parentalcontrol.dao.ForegroundAppDao;

import static fei.tcc.parentalcontrol.component.ForegroundProcessManager.getForegroundApp;

public class AppUsageInfoService extends IntentService {

    private static final String TAG = "AppUsageInfoService";

    private static final Integer DELAY_SCREEN_UNLOCKED = 2000; // 2 seconds in ms

    private static final Integer DELAY_SCREEN_LOCKED = 15000; // 15 seconds in ms

    private ForegroundAppDao foregroundAppDao;

    public AppUsageInfoService(String name) {
        super(name);
    }

    public AppUsageInfoService() {
        this(AppUsageInfoService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        foregroundAppDao = new ForegroundAppDao(this);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // only get information about the foreground app when it is with phone unlocked
                if (!isScreenLocked()) {
                    Log.d(TAG, "Phone is unlocked");

                    String foregroundApp = getForegroundApp();
                    long currentTime = System.currentTimeMillis();

                    String appName = getAppName(foregroundApp);

                    Timestamp timestamp = new Timestamp(currentTime);
                    foregroundAppDao.insert(appName, (currentTime / 1000) * 1000);

                    Log.d(TAG, "App name saved: " + appName + " - Time: " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(timestamp));

                    handler.postDelayed(this, DELAY_SCREEN_UNLOCKED);
                } else {
                    Log.d(TAG, "Phone is locked");
                    handler.postDelayed(this, DELAY_SCREEN_LOCKED);
                }

            }
        }, DELAY_SCREEN_UNLOCKED);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
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

    private String getAppName(String packageName) {
        final PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName,  0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

}
