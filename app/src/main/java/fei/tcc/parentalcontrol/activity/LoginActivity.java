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
import fei.tcc.parentalcontrol.rest.dto.ParentLoginDto;
import fei.tcc.parentalcontrol.rest.dto.ParentLoginIdResponseDto;
import fei.tcc.parentalcontrol.rest.dto.UserLoginIdResponseDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView linkRedirectRegister;

    private EditText email;

    private EditText password;

    private Button loginButton;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(LoginActivity.this);

        linkRedirectRegister = (TextView) findViewById(R.id.link_redirect_register);

        email = (EditText) findViewById(R.id.edit_text_email);
        password = (EditText) findViewById(R.id.edit_text_password);

        loginButton = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userDao.existsParent()) {
            redirectToActivity(DeviceRegisterActivity.class);
        }

        linkRedirectRegister.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_redirect_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                // Initalize Retrofit
                APIPlug apiPlug = RetrofitConfig.get();

                ParentLoginDto parentLoginDto = new ParentLoginDto();
                parentLoginDto.setEmail(email.getText().toString());
                parentLoginDto.setPassword(password.getText().toString());

                Call<ParentLoginIdResponseDto> userLoginIdResponseDtoCall = apiPlug.loginUserParent(parentLoginDto);
                userLoginIdResponseDtoCall.enqueue(new Callback<ParentLoginIdResponseDto>() {
                    @Override
                    public void onResponse(Call<ParentLoginIdResponseDto> call, Response<ParentLoginIdResponseDto> response) {
                        ParentLoginIdResponseDto newUser = response.body();

                        if (!newUser.getLogged() || newUser.getParentId() == -1) {
                            Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Integer parentId = newUser.getParentId();

                        userDao.insertParent(parentId);

                        redirectToActivity(DeviceRegisterActivity.class);
                    }

                    @Override
                    public void onFailure(Call<ParentLoginIdResponseDto> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Falha ao logar usuário!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private void redirectToActivity(Class<?> cls) {
        Intent appUsageActivity = new Intent(LoginActivity.this, cls);
        startActivity(appUsageActivity);
    }
}
