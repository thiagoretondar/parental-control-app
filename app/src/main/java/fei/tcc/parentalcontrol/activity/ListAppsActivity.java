package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.adapter.SelectableAdapter;
import fei.tcc.parentalcontrol.utils.BlackListPackageName;
import fei.tcc.parentalcontrol.vo.AppVo;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

public class ListAppsActivity extends AppCompatActivity {

    private static final String TAG = ListAppsActivity.class.getSimpleName();

    private UsageStatsManager mUsageStatsManager;

    private Button mOpenUsageSettingButton;

    private ListView appListView;

    private SelectableAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apps);

        mUsageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

        mOpenUsageSettingButton = (Button) findViewById(R.id.button_open_usage_setting);

        appListView = (ListView) findViewById(R.id.list_apps);

        // Obtain the installed apps in system
        List<AppVo> apps = getInstalledApps();

        sAdapter = new SelectableAdapter(this, android.R.layout.simple_list_item_1, apps);

        appListView.setAdapter(sAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu_action, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.permission_btn:

                Intent appUsageActivity = new Intent(ListAppsActivity.this, AppPermissionActivity.class);
                startActivity(appUsageActivity);

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Use PackageManager to get the list of installed apps
     *
     * @return list of apps installed
     */
    @NonNull
    private List<AppVo> getInstalledApps() {

        // PackageManager to get info about apps
        PackageManager pm = this.getPackageManager();

        // Get the info about usage of the apps
        List<UsageStats> usageStatistics = getUsageStatistics(UsageStatsManager.INTERVAL_YEARLY);

        List<AppVo> apps = new ArrayList<>();
        for (UsageStats usageStat : usageStatistics) {
            String packageName = usageStat.getPackageName();
            if (!BlackListPackageName.has(packageName)) {
                AppVo app = new AppVo();

                try {
                    // TODO what is zero?
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                    Drawable applicationIcon = pm.getApplicationIcon(applicationInfo);
                    String applicationName = pm.getApplicationLabel(applicationInfo).toString();

                    app.setName(applicationName);
                    app.setIcon(applicationIcon);
                    app.setUsageStats(usageStat);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.w(TAG, "ApplicationInfo not found with package " + packageName);
                    e.printStackTrace();
                }

                apps.add(app);
            }
        }

        return apps;
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the acscess to apps usage. ");
            Toast.makeText(this,
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }

        return queryUsageStats;
    }
}
