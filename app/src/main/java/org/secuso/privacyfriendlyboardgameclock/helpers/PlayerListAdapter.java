package org.secuso.privacyfriendlyboardgameclock.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.model.Player;

import java.util.List;

/**
 * Created by Quang Anh Dang on 01.12.2017.
 * https://guides.codepath.com/android/Using-the-RecyclerView#creating-the-recyclerview-adapter
 * https://www.youtube.com/watch?v=puyiZKvxBa0
 * TODO DOC
 */
public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
    private List<Player> playersList;
    private Activity activity;
    private ItemClickListener itemClickListener;

    public PlayerListAdapter(Activity activity, List<Player> playersList) {
        this.playersList = playersList;
        this.activity = activity;
    }

    public Context getContext() {
        return activity;
    }

    /**
     * Usually involves inflating a layout from XML and returning the holder
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = activity.getLayoutInflater();

        // Inflate the custom layout
        View playersView = inflater.inflate(R.layout.player_management_custom_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(playersView);
        return viewHolder;
    }

    /**
     * Involves populating data into the item through holder
     * @param viewHolder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PlayerListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Player player = playersList.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.playerTextView;
        textView.setText(player.getName());
        ImageView imageView = viewHolder.playerIMGView;
        imageView.setImageBitmap(player.getIcon());

        // Set ItemClickListener here
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    //TODO what happen if longclicked, change to delete mode
                }
                else{
                    //TODO if normal click, show dialog with player
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                    LayoutInflater factory = activity.getLayoutInflater();
                    View diaglogView = factory.inflate(R.layout.dialog_show_player_details,null);

                    // set the custom dialog components - texts and image
                    TextView name = (TextView) diaglogView.findViewById(R.id.dialog_player_name);
                    name.setText(playersList.get(position).getName());
                    ImageView icon = (ImageView) diaglogView.findViewById(R.id.dialog_player_image);
                    icon.setImageBitmap(playersList.get(position).getIcon());

                    dialogBuilder.setTitle("Position " + position)
                            .setView(diaglogView)
                            .setCancelable(true)// dismiss when touching outside Dialog
                            .create().show();
                }
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return (playersList != null ? playersList.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView playerIMGView;
        private TextView playerTextView;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            playerIMGView = (ImageView) itemView.findViewById(R.id.player_image);
            playerTextView = (TextView) itemView.findViewById(R.id.player_text);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }
}