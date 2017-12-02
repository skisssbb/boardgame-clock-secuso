package org.secuso.privacyfriendlyboardgameclock.activities;

import android.os.Bundle;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.database.PlayersDataSourceSingleton;

public class PlayerManagementActivity extends BaseActivity {
    private PlayersDataSourceSingleton pds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pds = PlayersDataSourceSingleton.getInstance(getApplicationContext());
        pds.open();
        setContentView(R.layout.activity_player_management);
    }

    @Override
    protected int getNavigationDrawerID() {return R.id.nav_player_management;}
}