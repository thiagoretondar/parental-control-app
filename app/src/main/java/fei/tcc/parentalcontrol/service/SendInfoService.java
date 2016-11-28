package fei.tcc.parentalcontrol.service;

import android.app.IntentService;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fei.tcc.parentalcontrol.config.RetrofitConfig;
import fei.tcc.parentalcontrol.dao.ForegroundAppDao;
import fei.tcc.parentalcontrol.dao.LocationDao;
import fei.tcc.parentalcontrol.dao.UserDao;
import fei.tcc.parentalcontrol.rest.APIPlug;
import fei.tcc.parentalcontrol.rest.dto.AllAppsInfoDto;
import fei.tcc.parentalcontrol.rest.dto.AppUsageInfoDto;
import fei.tcc.parentalcontrol.rest.dto.LastDatetimeUsedDto;
import fei.tcc.parentalcontrol.rest.dto.LocationInfoDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendInfoService extends IntentService {

    private static final String TAG = "REST SERVICE";

    private UsageStatsManager mUsageStatsManager;

    private ForegroundAppDao foregroundAppDao;

    private LocationDao locationDao;

    private static final Integer SEND_INFO_TIME = 300000;

    private UserDao userDao;

    public SendInfoService(String name) {
        super(name);
    }

    public SendInfoService() {
        this(SendInfoService.class.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get usage stats manager
        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        // Initalize DAOs
        foregroundAppDao = new ForegroundAppDao(this);
        locationDao = new LocationDao(this);
        userDao = new UserDao(this);

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

                // Set USER ID in DTO
                allAppsInfoDto.setUserId(userDao.selectIdFromUser());

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

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
