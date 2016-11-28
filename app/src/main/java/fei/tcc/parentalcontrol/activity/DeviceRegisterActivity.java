package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.config.RetrofitConfig;
import fei.tcc.parentalcontrol.dao.UserDao;
import fei.tcc.parentalcontrol.rest.APIPlug;
import fei.tcc.parentalcontrol.rest.dto.UserChildCreationDto;
import fei.tcc.parentalcontrol.rest.dto.UserLoginIdResponseDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;

    private Spinner sexOptions;

    private EditText day;

    private EditText month;

    private EditText year;

    private Button registerChildButton;

    private UserDao userDao;

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_register);

        userDao = new UserDao(DeviceRegisterActivity.this);

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        name = (EditText) findViewById(R.id.edit_text_user_name);
        sexOptions = (Spinner) findViewById(R.id.spinner_user_sex);
        day = (EditText) findViewById(R.id.edit_text_user_day);
        month = (EditText) findViewById(R.id.edit_text_user_month);
        year = (EditText) findViewById(R.id.edit_text_user_year);
        registerChildButton = (Button) findViewById(R.id.btn_create_child_account);

    }

    @Override
    protected void onResume() {

        if (userDao.existsParent() && userDao.existsUser()) {
            redirectToActivity(ListAppsActivity.class);
        }

        registerChildButton.setOnClickListener(this);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_child_account:
                // Initalize Retrofit
                APIPlug apiPlug = RetrofitConfig.get();

                String yearStr = year.getText().toString();
                String monthStr = month.getText().toString();
                String dayStr = day.getText().toString();

                UserChildCreationDto userChildCreationDto = new UserChildCreationDto();
                userChildCreationDto.setName(name.getText().toString());
                userChildCreationDto.setSex(sexOptions.getSelectedItem().toString().toLowerCase());
                userChildCreationDto.setDeviceId(deviceId);
                userChildCreationDto.setParentId(userDao.selectParentId());
                userChildCreationDto.setBirthdate(yearStr + "-" + monthStr + "-" + dayStr);

                Call<UserLoginIdResponseDto> userChildCall = apiPlug.createUserChild(userChildCreationDto);
                userChildCall.enqueue(new Callback<UserLoginIdResponseDto>() {
                    @Override
                    public void onResponse(Call<UserLoginIdResponseDto> call, Response<UserLoginIdResponseDto> response) {
                        UserLoginIdResponseDto newUserChildResponse = response.body();

                        Integer userId = newUserChildResponse.getUserId();
                        if (userId == -1 || !newUserChildResponse.isRegistered()) {
                            Toast.makeText(DeviceRegisterActivity.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer parentId = userDao.selectParentId();

                        userDao.insertUser(userId, deviceId, parentId);

                        redirectToActivity(AppPermissionActivity.class);
                    }

                    @Override
                    public void onFailure(Call<UserLoginIdResponseDto> call, Throwable t) {
                        Toast.makeText(DeviceRegisterActivity.this, "Falha ao logar usuário!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private void redirectToActivity(Class<?> cls) {
        Intent appUsageActivity = new Intent(DeviceRegisterActivity.this, cls);
        startActivity(appUsageActivity);
    }
}
