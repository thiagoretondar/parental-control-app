package fei.tcc.parentalcontrol.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import fei.tcc.parentalcontrol.R;

public class ListAppsActivity extends AppCompatActivity {

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

    }
}
