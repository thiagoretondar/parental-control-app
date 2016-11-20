package fei.tcc.parentalcontrol.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import fei.tcc.parentalcontrol.R;
import fei.tcc.parentalcontrol.dao.PackageDao;

public class AppUsageActivity extends AppCompatActivity {

    private static final String TAG = ListAppsActivity.class.getSimpleName();

    private ListView packageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = new Intent(this, AppUsageInfoService.class);

        setContentView(R.layout.activity_app_usage);
        packageView = (ListView) findViewById(R.id.list_package);

        //startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadPackageList();

    }

    private void loadPackageList() {
        PackageDao packageDao = new PackageDao(this);
        List<String> allPackages = packageDao.findAllPackages();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allPackages);
        packageView.setAdapter(adapter);
    }
}
