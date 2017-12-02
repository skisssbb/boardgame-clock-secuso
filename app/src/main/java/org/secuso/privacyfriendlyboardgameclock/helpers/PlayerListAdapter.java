package org.secuso.privacyfriendlyboardgameclock.helpers;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.model.Player;

import java.util.List;

public class PlayerListAdapter extends ArrayAdapter { //--CloneChangeRequired
    private List playersList; //--CloneChangeRequired
    private Context mContext;


    /**
     *
     * @param context The current context aka current Activity
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param list The objects to represent in the ListView.
     */
    public PlayerListAdapter(Context context, int textViewResourceId,
                             List list) { //--CloneChangeRequired
        super(context, textViewResourceId, list);
        this.playersList = list;
        this.mContext = context;
    }

    /**
     * This class deals with how the passed in List Elements to be displayed
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View playerListView = convertView;
        try {
            // Reuse view if already exist, otherwise create new one
            if(playerListView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                playerListView = inflater.inflate(R.layout.player_management_custom_row, parent, false);
            }
            Player p = (Player) getItem(position);
            TextView pText = (TextView) playerListView.findViewById(R.id.player_text);
            ImageView pImage = (ImageView) playerListView.findViewById(R.id.player_image);

            pText.setText(p.getName());
            pImage.setImageBitmap(p.getIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerListView;
    }
}