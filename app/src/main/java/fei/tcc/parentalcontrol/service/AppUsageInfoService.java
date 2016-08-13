package fei.tcc.parentalcontrol.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import fei.tcc.parentalcontrol.component.ProcessManager;
import fei.tcc.parentalcontrol.dao.PackageDao;

public class AppUsageInfoService extends IntentService {

    private static final String TAG = "MyService";

    private UsageStatsManager usageStatsManager;

    public AppUsageInfoService(String name) {
        super(name);
    }

    public AppUsageInfoService() {
        this(AppUsageInfoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        usageStatsManager = ((UsageStatsManager) this.getSystemService("usagestats"));
        Log.i(TAG, "Antes do RUN");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //AppUsageInfoService.this.getForegroundApp();
//                Log.i(TAG, "FOREGROUND APP: " + getApplicationInForeground());

                List<ActivityManager.RunningAppProcessInfo> procInfos=getRunningAps();
                int loop=0;
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : procInfos) {
                    //System.out.println("Process Name=====>"+procInfos.get(loop).processName);
                    Log.i(TAG, "Process Name=====>"+procInfos.get(loop).processName);
                    if(procInfos.get(loop).importance== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                    }
                    ++loop;
                }
                handler.postDelayed(this, 3000);
            }
        }, 2000);

        return super.onStartCommand(intent, flags, startId);

       // return super.onStartCommand(intent, flags, startId);
    }

    public void getForegroundApp() {
        PackageDao packageDao = new PackageDao(this);

        String foregroundApp = ProcessManager.getForegroundApp();

        packageDao.insert(foregroundApp);

        Log.i(TAG, "FOREGROUND APP: " + foregroundApp);

        packageDao.close();

    }

    public String getApplicationInForeground() {
        long time = System.currentTimeMillis();
        UsageEvents usageEvent = usageStatsManager.queryEvents(time - 100 * 1000, time);
        UsageEvents.Event event = new UsageEvents.Event();

        while (usageEvent.hasNextEvent()) {
            usageEvent.getNextEvent(event);
        }
        if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
            return event.getPackageName();
        }
        return "ANY PACKAGE FOUND";
    }

    /**
     * This method returns list of running application.
     * @return List<RunningAppProcessInfo>: containing application informations.
     */
    private List<ActivityManager.RunningAppProcessInfo> getRunningAps(){
        ActivityManager actvityManager = (ActivityManager)this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
        return procInfos;
    }

}
