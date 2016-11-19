package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.dao.UserDao;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;

    private Button loginButton;

    private UserDao userDao;

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = new UserDao(MainActivity.this);

        loginButton = (Button) findViewById(R.id.login_btn);
        registerButton = (Button) findViewById(R.id.register_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userDao.existsUser()) {
            redirectToAppUsageActivity();
        }

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.register_btn:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private void redirectToAppUsageActivity() {
        Intent appUsageActivity = new Intent(MainActivity.this, ListAppsActivity.class);
        startActivity(appUsageActivity);
    }
}
