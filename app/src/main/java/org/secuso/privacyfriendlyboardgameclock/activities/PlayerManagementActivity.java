package org.secuso.privacyfriendlyboardgameclock.activities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.database.PlayersDataSourceSingleton;
import org.secuso.privacyfriendlyboardgameclock.fragments.PlayerManagementChooseModeFragment;
import org.secuso.privacyfriendlyboardgameclock.helpers.PlayerListAdapter;
import org.secuso.privacyfriendlyboardgameclock.model.Player;

import java.util.List;

public class PlayerManagementActivity extends BaseActivity {
    private PlayersDataSourceSingleton pds;
    private RecyclerView playersRecycleView;
    private PlayerListAdapter playerListAdapter;
    private List<Player> listPlayers;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fabAdd;
    private int selectedPlayerId = -1;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_management);
        fm = getFragmentManager();
        pds = PlayersDataSourceSingleton.getInstance(getApplicationContext());
        pds.open();
        listPlayers = pds.getAllPlayers();
        listPlayers.add(new Player(112,2011457,"Player1", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(113,2011453,"Player2", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(114,2011454,"Player3", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(115,2011456,"Player4", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));

        layoutManager = new LinearLayoutManager(this);

        // Lookup the recyclerview in fragment layout
        playersRecycleView = findViewById(R.id.player_list);
        playersRecycleView.setHasFixedSize(true);
        playerListAdapter = new PlayerListAdapter(this, listPlayers);
        playersRecycleView.setAdapter(playerListAdapter);
        playersRecycleView.setLayoutManager(layoutManager);

        // FAB Listener
        fabAdd = findViewById(R.id.fab_add_new_player);
        fabAdd.setOnClickListener(onFABAddClickListener());
    }

    @Override
    protected int getNavigationDrawerID() {return R.id.nav_player_management;}

    private View.OnClickListener onFABAddClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if(prev != null) ft.remove(prev);
                ft.addToBackStack(null);

                // Create and show the dialog
                PlayerManagementChooseModeFragment chooseDialogFragment = PlayerManagementChooseModeFragment.newInstance("Choose how to create new player:");
                chooseDialogFragment.show(ft,"dialog");
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START) && fm.getBackStackEntryCount() > 0) {
            finish();
            startActivity(getIntent());
        }
        else super.onBackPressed();
    }
}