package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.vo.AppVo;

import static android.content.pm.PackageManager.GET_META_DATA;

public class ListAppsActivity extends AppCompatActivity {

    private UsageStatsManager usageStatsManager;

    private ListView appListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apps);

        appListView = (ListView) findViewById(R.id.list_apps);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAppInstalledList();
    }

    private void loadAppInstalledList() {
        List<AppVo> apps = new ArrayList<>();

        // get app list of used apps
        PackageManager pm = this.getPackageManager();
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(GET_META_DATA);
        for (ApplicationInfo ai : installedApplications) {
            String appName = pm.getApplicationLabel(ai).toString();
            Drawable appIcon = pm.getApplicationIcon(ai);

            AppVo appVo = new AppVo();
            appVo.setName(appName);
            appVo.setIcon(appIcon);

            apps.add(appVo);
        }

        ArrayAdapter<AppVo> appsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, apps);

        appListView.setAdapter(appsAdapter);
    }
}
