package fei.tcc.parentalcontrol.service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fei.tcc.parentalcontrol.config.RetrofitConfig;
import fei.tcc.parentalcontrol.dao.ForegroundAppDao;
import fei.tcc.parentalcontrol.dao.LocationDao;
import fei.tcc.parentalcontrol.rest.APIPlug;
import fei.tcc.parentalcontrol.rest.dto.AllAppsInfoDto;
import fei.tcc.parentalcontrol.rest.dto.AppUsageInfoDto;
import fei.tcc.parentalcontrol.rest.dto.LastDatetimeUsedDto;
import fei.tcc.parentalcontrol.rest.dto.LocationInfoDto;
import fei.tcc.parentalcontrol.rest.dto.MostUsedAppsDto;
import fei.tcc.parentalcontrol.utils.BlackListPackageName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Collections.sort;

public class SendInfoService extends Service {

    private static final String TAG = "REST SERVICE";

    private UsageStatsManager mUsageStatsManager;

    private ForegroundAppDao foregroundAppDao;

    private LocationDao locationDao;

    private static final Integer SEND_INFO_TIME = 300000;

    private static final Integer TOTAL_MOST_USED_APPS = 3;

    public SendInfoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get usage stats manager
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        // Initalize DAOs
        foregroundAppDao = new ForegroundAppDao(this);
        locationDao = new LocationDao(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // Creates DTO which will be sent
                AllAppsInfoDto allAppsInfoDto = new AllAppsInfoDto();

                // Initalize Retrofit
                APIPlug apiPlug = RetrofitConfig.get();

                // Get all the apps and the timestamp which one was used
                Map<String, List<Long>> allAppsUsage = foregroundAppDao.selectAllAppsUsage();

                // Put all app usage information in DTO
                List<AppUsageInfoDto> allAppsUsageList = new ArrayList<>();
                for (String appUsageKey : allAppsUsage.keySet()) {
                    AppUsageInfoDto appUsageInfoDto = new AppUsageInfoDto();
                    appUsageInfoDto.setAppName(appUsageKey);
                    appUsageInfoDto.setDateTimes(allAppsUsage.get(appUsageKey));

                    allAppsUsageList.add(appUsageInfoDto);
                }
                allAppsInfoDto.setAppUsageInfoList(allAppsUsageList);


                // Get all the locations where phone was used
                Map<Long, List<Double>> allLocationsMap = locationDao.selectAllLocations();
                Log.d("APIREST", "MAX TIMESTAMP IN LOCATION: " + locationDao.getMaxTimestamp());
                // Put all app location information in DTO
                List<LocationInfoDto> appLocationList = new ArrayList<>();
                for (Long locationKey : allLocationsMap.keySet()) {
                    LocationInfoDto appLocation = new LocationInfoDto();
                    appLocation.setDatetime(locationKey);
                    appLocation.setLatitude(allLocationsMap.get(locationKey).get(0));
                    appLocation.setLongitude(allLocationsMap.get(locationKey).get(1));

                    appLocationList.add(appLocation);
                }
                allAppsInfoDto.setLocationInfoList(appLocationList);

                // Obtain the installed apps in system
                List<MostUsedAppsDto> mostUsedAppsList = getInstalledApps();
                allAppsInfoDto.setMostUsedAppsList(mostUsedAppsList);

                // Set USER ID in DTO
                allAppsInfoDto.setUserId(1L);

                Log.d("APIREST", "Enviando para /app");
                final Call<LastDatetimeUsedDto> stringCall = apiPlug.sendAllAppsInfo(allAppsInfoDto);

                stringCall.enqueue(new Callback<LastDatetimeUsedDto>() {
                    @Override
                    public void onResponse(Call<LastDatetimeUsedDto> call, Response<LastDatetimeUsedDto> response) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                        LastDatetimeUsedDto bodyResponse = response.body();

                        String lastDateAppUsage = simpleDateFormat.format(bodyResponse.getLastAppUsageDatetime());
                        Log.d("APIREST", "Received lastApp: " + lastDateAppUsage);
                        Log.d("APIREST", "Before delete from database: " + foregroundAppDao.countAll());
                        foregroundAppDao.deleteAllUntil(bodyResponse.getLastAppUsageDatetime());
                        Log.d("APIREST", "After delete from database: " + foregroundAppDao.countAll());

                        String lastDateLocationUsage = simpleDateFormat.format(bodyResponse.getLastLocationUsageDatetime());
                        Log.d("APIREST", "Received lastLocation: " + lastDateLocationUsage);
                        Log.d("APIREST", "Before delete from database: " + locationDao.countAll());
                        locationDao.deleteAllUntil(bodyResponse.getLastLocationUsageDatetime());
                        Log.d("APIREST", "After delete from database: " + locationDao.countAll());

                    }

                    @Override
                    public void onFailure(Call<LastDatetimeUsedDto> call, Throwable t) {
                        Log.d("APIREST", "FAILED para /app", t);
                    }
                });
                handler.postDelayed(this, SEND_INFO_TIME);
            }
        }, SEND_INFO_TIME);


        return super.onStartCommand(intent, flags, startId);
    }



    /**
     * Use PackageManager to get the list of installed apps
     *
     * @return list of apps installed
     */
    @NonNull
    private List<MostUsedAppsDto> getInstalledApps() {

        // PackageManager to get info about apps
        PackageManager pm = this.getPackageManager();

        // Get the info about usage of the apps
        List<UsageStats> usageStatistics = getUsageStatistics(UsageStatsManager.INTERVAL_YEARLY);

        List<MostUsedAppsDto> apps = new ArrayList<>();
        for (UsageStats usageStat : usageStatistics) {
            if (!BlackListPackageName.has(usageStat.getPackageName())) {

                String packageName = usageStat.getPackageName();

                try {
                    // TODO what is zero?
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                    String applicationName = pm.getApplicationLabel(applicationInfo).toString();

                    int totalMinutes = (int) (usageStat.getTotalTimeInForeground() / (1000 * 60)) % 60;

                    int hours = totalMinutes / 60;
                    int minutes = totalMinutes % 60;


                    // only add if hours and minutes are different than zero
                    if (hours != 0 || minutes != 0) {
                        MostUsedAppsDto mostUsedAppsDto = new MostUsedAppsDto();
                        mostUsedAppsDto.setName(applicationName);
                        mostUsedAppsDto.setHours(hours);
                        mostUsedAppsDto.setMinutes(minutes);

                        apps.add(mostUsedAppsDto);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.w(TAG, "ApplicationInfo not found with package " + packageName);
                }

            }
        }

        // sort apps by most used
        sort(apps, new Comparator<MostUsedAppsDto>() {
            @Override
            public int compare(MostUsedAppsDto app1, MostUsedAppsDto app2) {
                long app1Minutes = app1.getHours() * 60 + app1.getMinutes();
                long app2Minutes = app2.getHours() * 60 + app2.getMinutes();

                if (app1Minutes == app2Minutes) {
                    return 0;
                } else if (app2Minutes > app1Minutes) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        if (apps.size() > TOTAL_MOST_USED_APPS) {
            return new ArrayList<>(apps.subList(0, 3));
        } else {
            return apps;
        }
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

//        if (queryUsageStats.size() == 0) {
//            Log.i(TAG, "The user may not allow the acscess to apps usage. ");
//            Toast.makeText(this,
//                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
//                    Toast.LENGTH_LONG).show();
//            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
//            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//                }
//            });
//        }

        return queryUsageStats;
    }
}
