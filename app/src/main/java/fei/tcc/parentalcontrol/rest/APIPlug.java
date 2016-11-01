package fei.tcc.parentalcontrol.rest;

import fei.tcc.parentalcontrol.rest.dto.AllAppsInfoDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by thiagoretondar on 9/12/16.
 */
public interface APIPlug {

    @POST("/app")
    Call<String> sendAllAppsInfo(@Body AllAppsInfoDto allAppsInfoDto);

}
