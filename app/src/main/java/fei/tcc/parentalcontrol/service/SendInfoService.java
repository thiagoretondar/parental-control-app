package fei.tcc.parentalcontrol.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fei.tcc.parentalcontrol.config.RetrofitConfig;
import fei.tcc.parentalcontrol.rest.APIPlug;
import fei.tcc.parentalcontrol.rest.dto.AllAppsInfoDto;
import fei.tcc.parentalcontrol.rest.dto.AppUsageInfoDto;
import fei.tcc.parentalcontrol.rest.dto.LocationInfoDto;
import fei.tcc.parentalcontrol.rest.dto.MostUsedAppsDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static java.util.Arrays.asList;

public class SendInfoService extends Service {
    public SendInfoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        APIPlug apiPlug = RetrofitConfig.get();

        AllAppsInfoDto allAppsInfoDto = new AllAppsInfoDto();

        List<AppUsageInfoDto> appUsageList = new ArrayList<>();
        // make loop in database and remove those data from info
        AppUsageInfoDto appUsage1 = new AppUsageInfoDto();
        appUsage1.setAppName("Test 1");
        appUsage1.setDateTimes(asList("2016-10-31T21:57:25", "2016-10-31T21:57:23"));
        appUsageList.add(appUsage1);
        allAppsInfoDto.setAppUsageInfoList(appUsageList);

        List<LocationInfoDto> appLocationList = new ArrayList<>();
        // make loop in database and remove those data from info
        LocationInfoDto appLocation1 = new LocationInfoDto();
        appLocation1.setDatetime("2016-10-31T21:57:23");
        appLocation1.setLatitude(-12.12324242D);
        appLocation1.setLongitude(-24.12324242D);
        appLocationList.add(appLocation1);
        allAppsInfoDto.setLocationInfoList(appLocationList);


        List<MostUsedAppsDto> mostUsedAppsList = new ArrayList<>();
        // make loop in database and remove those data from info
        MostUsedAppsDto mostUsedApp1 = new MostUsedAppsDto();
        mostUsedApp1.setName("Test 1");
        mostUsedApp1.setTime("99:99");
        mostUsedAppsList.add(mostUsedApp1);
        allAppsInfoDto.setMostUsedAppsList(mostUsedAppsList);

        allAppsInfoDto.setUserId(1L);

        Log.d("APIREST", "Enviando para /app");
        final Call<String> stringCall = apiPlug.sendAllAppsInfo(allAppsInfoDto);

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("APIREST", call.request().body().toString());
                Log.d("APIREST", "SENDED para /app");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("APIREST", "FAILED para /app", t);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
