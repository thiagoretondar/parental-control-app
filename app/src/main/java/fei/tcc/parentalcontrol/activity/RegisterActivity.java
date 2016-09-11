package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fei.tcc.parentalcontrol.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView linkRedirectLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        linkRedirectLogin = (TextView) findViewById(R.id.link_redirect_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        linkRedirectLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_redirect_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
