package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.adapter.SelectableAdapter;
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

        mUsageStatsManager = (UsageStatsManager) this.getSystemService("usagestats"); //Context.USAGE_STATS_SERVICE

        mOpenUsageSettingButton = (Button) findViewById(R.id.button_open_usage_setting);

        appListView = (ListView) findViewById(R.id.list_apps);

        appListView.setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);

        appListView.setMultiChoiceModeListener(actionOnItemClicked());


        // Obtain the installed apps in system
        List<AppVo> apps = getInstalledApps();

        sAdapter = new SelectableAdapter(this, android.R.layout.simple_list_item_multiple_choice, apps);

        appListView.setAdapter(sAdapter);

        selectAllItemsByDefault(apps);

    }

    /**
     * Select all apps passed
     *
     * @param apps apps to be selected
     */
    private void selectAllItemsByDefault(List<AppVo> apps) {
        for (int i = 0; i < apps.size(); i++) {
            appListView.setItemChecked(i, true);
        }
    }

    /**
     * Implements methods to select multiple items of ListView
     *
     * @return action when click in one item of list
     */
    @NonNull
    private MultiChoiceModeListener actionOnItemClicked() {
        return new MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                // Prints the count of selected Items in title
                actionMode.setTitle(appListView.getCheckedItemCount() + " Selected");

                // Toggle the state of item after every click on it
                sAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.app_menu_action, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.delete) {
                    SparseBooleanArray selected = sAdapter.getSelectedIds();
                    short size = (short) selected.size();
                    for (byte i = 0; i < size; i++) {
                        if (selected.valueAt(i)) {
                            AppVo selectedItem = sAdapter.getItem(selected.keyAt(i));
                            sAdapter.remove(selectedItem);
                        }
                    }

                    // Close CAB (Contextual Action Bar)
                    actionMode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        };
    }

    /**
     * Use PackageManager to get the list of installed apps
     *
     * @return list of apps installed
     */
    @NonNull
    private List<AppVo> getInstalledApps() {
        
        // get app list of used apps
//        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(GET_META_DATA);
//        for (ApplicationInfo ai : installedApplications) {
//            String appName = pm.getApplicationLabel(ai).toString();
//            Drawable appIcon = pm.getApplicationIcon(ai);
//
//            AppVo appVo = new AppVo();
//            appVo.setName(appName);
//            appVo.setIcon(appIcon);
//
//            apps.add(appVo);
//        }

        // PackageManager to get info about apps
        PackageManager pm = this.getPackageManager();

        // Get the info about usage of the apps
        List<UsageStats> usageStatistics = getUsageStatistics(UsageStatsManager.INTERVAL_YEARLY);

        List<AppVo> apps = new ArrayList<>();
        for (UsageStats usageStat : usageStatistics) {
            AppVo app = new AppVo();

            String packageName = usageStat.getPackageName();

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

        return apps;
    }

    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);

        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        List<UsageStats> newUsage = new ArrayList<>(1);
        for (UsageStats u : queryUsageStats) {
            if (u.getPackageName().equals("com.android.mms")) {
                newUsage.add(u);
            }
        }

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
