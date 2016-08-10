package fei.tcc.parentalcontrol.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import fei.tcc.parentalcontrol.component.ProcessManager;
import fei.tcc.parentalcontrol.dao.PackageDao;

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
                AppUsageInfoService.this.getForegroundApp();
                handler.postDelayed(this, 1000);
            }
        }, 2000);

        return super.onStartCommand(intent, flags, startId);
    }

    public void getForegroundApp() {
        PackageDao packageDao = new PackageDao(this);

        packageDao.insert(ProcessManager.getForegroundApp());

        packageDao.close();

//        Log.i(TAG, "FOREGROUND APP: " + ProcessManager.getForegroundApp());
    }
}
