package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.config.RetrofitConfig;
import fei.tcc.parentalcontrol.dao.UserDao;
import fei.tcc.parentalcontrol.rest.APIPlug;
import fei.tcc.parentalcontrol.rest.dto.ParentCreationDto;
import fei.tcc.parentalcontrol.rest.dto.UserLoginIdResponseDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView linkRedirectLogin;

    private EditText firstname;

    private EditText lastname;

    private EditText email;

    private EditText password;

    private Button createAccountBtn;

    private UserDao userDao;

    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDao = new UserDao(RegisterActivity.this);

        firstname = (EditText) findViewById(R.id.edit_text_parent_firstname);
        lastname = (EditText) findViewById(R.id.edit_text_parent_lastname);
        email = (EditText) findViewById(R.id.edit_text_parent_email);
        password = (EditText) findViewById(R.id.edit_text_parent_password);

        createAccountBtn = (Button) findViewById(R.id.btn_create_account);

        linkRedirectLogin = (TextView) findViewById(R.id.link_redirect_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userDao.existsUser()) {
            redirectToActivity(ListAppsActivity.class);
        }

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        linkRedirectLogin.setOnClickListener(this);
        createAccountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_redirect_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_create_account:
                // Initalize Retrofit
                APIPlug apiPlug = RetrofitConfig.get();

                ParentCreationDto parentCreationDto = new ParentCreationDto();
                parentCreationDto.setFirstName(firstname.getText().toString());
                parentCreationDto.setLastName(lastname.getText().toString());
                parentCreationDto.setEmail(email.getText().toString());
                parentCreationDto.setPassword(password.getText().toString());

                Call<UserLoginIdResponseDto> newParentCall = apiPlug.createNewParentUser(parentCreationDto);
                newParentCall.enqueue(new Callback<UserLoginIdResponseDto>() {
                    @Override
                    public void onResponse(Call<UserLoginIdResponseDto> call, Response<UserLoginIdResponseDto> response) {
                        UserLoginIdResponseDto newUser = response.body();

                        if (!newUser.getLogged() || newUser.getUserId() == -1) {
                            Toast.makeText(RegisterActivity.this, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer userId = newUser.getUserId();

                        userDao.insert(userId, deviceId);

                        redirectToActivity(AppPermissionActivity.class);
                    }

                    @Override
                    public void onFailure(Call<UserLoginIdResponseDto> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Falha ao criar usuário!", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    private void redirectToActivity(Class<?> cls) {
        Intent appUsageActivity = new Intent(RegisterActivity.this, cls);
        startActivity(appUsageActivity);
    }
}
