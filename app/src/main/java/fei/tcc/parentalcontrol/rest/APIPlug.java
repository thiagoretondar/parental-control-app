package fei.tcc.parentalcontrol.rest;

import fei.tcc.parentalcontrol.rest.dto.AllAppsInfoDto;
import fei.tcc.parentalcontrol.rest.dto.LastDatetimeUsedDto;
import fei.tcc.parentalcontrol.rest.dto.ParentCreationDto;
import fei.tcc.parentalcontrol.rest.dto.ParentLoginDto;
import fei.tcc.parentalcontrol.rest.dto.ParentLoginIdResponseDto;
import fei.tcc.parentalcontrol.rest.dto.UserChildCreationDto;
import fei.tcc.parentalcontrol.rest.dto.UserLoginIdResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by thiagoretondar on 9/12/16.
 */
public interface APIPlug {

    @POST("/app")
    Call<LastDatetimeUsedDto> sendAllAppsInfo(@Body AllAppsInfoDto allAppsInfoDto);

    @POST("/parent/create")
    Call<ParentLoginIdResponseDto> createNewParentUser(@Body ParentCreationDto parentCreationDto);

    @POST("/parent/login")
    Call<ParentLoginIdResponseDto> loginUserParent(@Body ParentLoginDto parentLoginDto);

    @POST("/parent/user/create")
    Call<UserLoginIdResponseDto> createUserChild(@Body UserChildCreationDto userChildCreationDto);
}
