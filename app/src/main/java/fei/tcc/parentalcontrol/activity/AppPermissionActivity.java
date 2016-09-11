package fei.tcc.parentalcontrol.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fei.tcc.parentalcontrol.R;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Process.myUid;

public class AppPermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private Button locationPermissionButton;

    private Button packagesPermissionButton;

    private  AppOpsManager appOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_permission);

        appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);

        locationPermissionButton = (Button) findViewById(R.id.button_location_permission);
        packagesPermissionButton = (Button) findViewById(R.id.button_packages_permission);
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationPermissionButton.setOnClickListener(this);
        packagesPermissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_location_permission:
                if (checkLocationPermission(ACCESS_FINE_LOCATION)) {
                    Toast.makeText(AppPermissionActivity.this, "Permissão já concedida", Toast.LENGTH_SHORT).show();
                } else {
                    requestLocationPermission(ACCESS_FINE_LOCATION);
                }
                break;

            case R.id.button_packages_permission:
                int mode = appOps.checkOpNoThrow("android:get_usage_stats", myUid(), AppPermissionActivity.this.getPackageName());
                boolean granted = mode == MODE_ALLOWED;
                if (granted) {
                    Toast.makeText(AppPermissionActivity.this, "Permissão já concedida", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
                break;
        }
    }

    private boolean checkLocationPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (result == PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestLocationPermission(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }
}
