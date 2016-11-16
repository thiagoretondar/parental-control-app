package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fei.tcc.parentalcontrol.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerButton;

    private Button loginButton;

    private String deviceId;

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login_btn);
        registerButton = (Button) findViewById(R.id.register_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();

        deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        Log.i(TAG, deviceId);

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
}
