package fei.tcc.parentalcontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import fei.tcc.parentalcontrol.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView linkRedirectRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linkRedirectRegister = (TextView) findViewById(R.id.link_redirect_register);
    }

    @Override
    protected void onResume() {
        super.onResume();

        linkRedirectRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_redirect_register:
                startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
