package org.secuso.privacyfriendlyboardgameclock.activities;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.view.ActionMode;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.database.PlayersDataSourceSingleton;
import org.secuso.privacyfriendlyboardgameclock.fragments.PlayerManagementChooseModeFragment;
import org.secuso.privacyfriendlyboardgameclock.fragments.PlayerManagementEditPlayerFragment;
import org.secuso.privacyfriendlyboardgameclock.fragments.PlayerManagementStatisticsFragment;
import org.secuso.privacyfriendlyboardgameclock.helpers.ItemClickListener;
import org.secuso.privacyfriendlyboardgameclock.helpers.PlayerListAdapter;
import org.secuso.privacyfriendlyboardgameclock.helpers.TAGHelper;
import org.secuso.privacyfriendlyboardgameclock.model.Player;

import java.util.List;

/**
 * Actionbar Tutorial
 * http://www.vogella.com/tutorials/AndroidActionBar/article.html#exercise-using-the-contextual-action-mode
 * Selection State tutorial
 * https://enoent.fr/blog/2015/01/18/recyclerview-basics/
 */
public class PlayerManagementActivity extends BaseActivity implements ItemClickListener{
    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    private PlayersDataSourceSingleton pds;
    private RecyclerView playersRecycleView;
    private PlayerListAdapter playerListAdapter;
    private List<Player> listPlayers;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabDelete;
    private int selectedPlayerId = -1;
    private FragmentManager fm;
    // To toggle selection mode
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private Player playerToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_management);
        fm = getFragmentManager();
        // pds already opened in MainActivity
        pds = PlayersDataSourceSingleton.getInstance(getApplicationContext());

        listPlayers = pds.getAllPlayers();
        /*listPlayers.add(new Player(112,2011457,"Player1", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(113,2011453,"Player2", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(114,2011454,"Player3", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));
        listPlayers.add(new Player(115,2011456,"Player4", BitmapFactory.decodeResource(getResources(),R.drawable.privacyfriendlyappslogo)));*/

        layoutManager = new LinearLayoutManager(this);

        // FAB Listener
        fabAdd = findViewById(R.id.fab_add_new_player);
        fabAdd.setOnClickListener(onFABAddClickListener());
        fabDelete = findViewById(R.id.fab_delete_player);
        fabDelete.setOnClickListener(onFABDeleteListenter());

        // Lookup the recyclerview in fragment layout
        playersRecycleView = findViewById(R.id.player_list);
        playersRecycleView.setHasFixedSize(true);
        playerListAdapter = new PlayerListAdapter(this, listPlayers, this);
        playersRecycleView.setAdapter(playerListAdapter);
        playersRecycleView.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClick(View view, final int position) {
        if(actionMode != null){
            toggleSelection(position);
        } else{
            //TODO if normal click, show dialog with player
            List<Player> playersList = playerListAdapter.getPlayersList();
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater factory = getLayoutInflater();
            View diaglogView = factory.inflate(R.layout.dialog_show_player_details,null);

            // set the custom dialog components - texts and image
            TextView name = diaglogView.findViewById(R.id.dialog_player_name);
            name.setText(playersList.get(position).getName());
            ImageView icon = diaglogView.findViewById(R.id.dialog_player_image);
            icon.setImageBitmap(playersList.get(position).getIcon());

            // Set onClickListener for Edit Player Button
            final Button editPlayerButton = diaglogView.findViewById(R.id.edit_player_button);
            editPlayerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment prev = fm.findFragmentByTag(TAGHelper.DIALOG_FRAGMENT);
                    if(prev != null) ft.remove(prev);
                    ft.addToBackStack(null);
                    playerToEdit = playerListAdapter.getPlayer(position);

                    // Create and show the dialog
                    PlayerManagementEditPlayerFragment editPlayerFragment = PlayerManagementEditPlayerFragment.newInstance("Edit Player");
                    editPlayerFragment.show(ft,TAGHelper.DIALOG_FRAGMENT);
                }
            });

            // Set onClickListener for Player Statistic Button
            final Button playerStatisticButton = diaglogView.findViewById(R.id.player_statistic_button);
            playerStatisticButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft  = fm.beginTransaction();
                    Fragment prev = fm.findFragmentByTag(TAGHelper.DIALOG_FRAGMENT);
                    if(prev != null) ft.remove(prev);
                    ft.addToBackStack(null);
                    playerToEdit = playerListAdapter.getPlayer(position);

                    // Create and show the dialog
                    PlayerManagementStatisticsFragment playerStatisticFragment = PlayerManagementStatisticsFragment.newInstance("Player Statistic");
                    playerStatisticFragment.show(ft,TAGHelper.DIALOG_FRAGMENT);
                }
            });


            dialogBuilder.setTitle("Position " + position)
                    .setView(diaglogView)
                    .setCancelable(true)// dismiss when touching outside Dialog
                    .create().show();
        }
    }

    @Override
    public boolean onItemLongClicked(View view, int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    /**
     * Toggle the selection state of an item.
     *
     * If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).
     *
     * @param position Position of the item to toggle the selection state
     */
    private void toggleSelection(int position) {
        playerListAdapter.toggleSelection(position);
        int count = playerListAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    protected int getNavigationDrawerID() {return R.id.nav_player_management;}

    /**
     * open Dialogfragment for user to choose how to create new player
     * @return
     */
    private View.OnClickListener onFABAddClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fm.beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag(TAGHelper.DIALOG_FRAGMENT);
                if(prev != null) ft.remove(prev);
                ft.addToBackStack(null);

                // Create and show the dialog
                PlayerManagementChooseModeFragment chooseDialogFragment = PlayerManagementChooseModeFragment.newInstance("Choose how to create new player:");
                chooseDialogFragment.show(ft,TAGHelper.DIALOG_FRAGMENT);
            }
        };
    }

    /**
     * remove all the selected players
     * @return
     */
    private View.OnClickListener onFABDeleteListenter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerListAdapter.removeItems(playerListAdapter.getSelectedItems());
                actionMode.finish();
                actionMode = null;

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

    /**
     * Infalte the Actionicons on Toolbar, in this case the delete icon
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                if(actionMode == null)
                    actionMode = startSupportActionMode(actionModeCallback);
        }
        return true;
    }

    private void switchVisibilityOf2FABs(){
        if(fabAdd.getVisibility() == View.VISIBLE && fabDelete.getVisibility() == View.GONE){
            fabAdd.setVisibility(View.GONE);
            fabDelete.setVisibility(View.VISIBLE);
        }else{
            fabAdd.setVisibility(View.VISIBLE);
            fabDelete.setVisibility(View.GONE);
        }
    }

    public Player getPlayerToEdit() {
        return playerToEdit;
    }

    public void setPlayerToEdit(Player playerToEdit) {
        this.playerToEdit = playerToEdit;
    }

    /*  #################################################################
        #                                                               #
        #                       Helper class                            #
        #                                                               #
        #################################################################*/

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate (R.menu.selected_menu, menu);
            switchVisibilityOf2FABs();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            playerListAdapter.clearSelection();
            actionMode = null;
            switchVisibilityOf2FABs();
        }
    }
}