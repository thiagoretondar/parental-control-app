package fei.tcc.parentalcontrol.activity;

import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.adapter.SelectableAdapter;
import fei.tcc.parentalcontrol.vo.AppVo;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

public class ListAppsActivity extends AppCompatActivity {

    private UsageStatsManager usageStatsManager;

    private ListView appListView;

    private SelectableAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apps);

        appListView = (ListView) findViewById(R.id.list_apps);

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

        sAdapter = new SelectableAdapter(this, R.layout.activity_list_apps, apps);
        appListView.setAdapter(sAdapter);

        appListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppVo appItem = (AppVo) parent.getAdapter().getItem(position);
                String appName = appItem.getName();
                Toast.makeText(ListAppsActivity.this, appName, Toast.LENGTH_SHORT).show();
            }
        });

        appListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                appListView.setChoiceMode(CHOICE_MODE_MULTIPLE_MODAL);

                appListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
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
                });

                return false;
            }
        });

    }
}
