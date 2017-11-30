package org.secuso.privacyfriendlyboardgameclock.activities;

import android.os.Bundle;

import org.secuso.privacyfriendlyboardgameclock.R;

public class PlayerManagementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_management);
    }

    @Override
    protected int getNavigationDrawerID() {return R.id.nav_player_management;}
}