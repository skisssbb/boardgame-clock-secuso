package org.secuso.privacyfriendlyboardgameclock.fragments;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.activities.PlayerManagementActivity;
import org.secuso.privacyfriendlyboardgameclock.database.PlayersDataSourceSingleton;
import org.secuso.privacyfriendlyboardgameclock.helpers.PlayerListAdapter;
import org.secuso.privacyfriendlyboardgameclock.model.Player;
import java.util.List;

/**
 * Created by Quang Anh Dang on 01.12.2017.
 * TODO DOC
 */

public class PlayerManagementMainFragment extends Fragment {
    private View view;
    private ListView listViewPlayers;
    private List<Player> listPlayers;
    private PlayersDataSourceSingleton pds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_management, container, false);
        pds = PlayersDataSourceSingleton.getInstance(null);
        listPlayers = pds.getAllPlayers();
        ListAdapter playerListAdapter = new PlayerListAdapter(this.getActivity(), R.layout.fragment_player_management,listPlayers);
        listViewPlayers = (ListView) rootView.findViewById(R.id.player_list);
        listViewPlayers.setAdapter(playerListAdapter);
        listViewPlayers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tmp = "Clicked on Player";
                Toast toast = Toast.makeText(getActivity(),tmp,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return rootView;
    }
}
