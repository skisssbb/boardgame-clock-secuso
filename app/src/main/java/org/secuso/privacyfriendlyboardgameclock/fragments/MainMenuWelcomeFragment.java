package org.secuso.privacyfriendlyboardgameclock.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.secuso.privacyfriendlyboardgameclock.R;
import org.secuso.privacyfriendlyboardgameclock.activities.MainActivity;
import org.secuso.privacyfriendlyboardgameclock.database.GamesDataSourceSingleton;

/**
 * Created by Quang Anh Dang on 15.12.2017.
 */

public class MainMenuWelcomeFragment extends Fragment{
    private static Activity activity;
    private FragmentManager fm;
    public static View.OnClickListener resumeGameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.MainActivity_fragment_container, new MainMenuContinueGameFragment());
            fragmentTransaction.addToBackStack(activity.getString(R.string.loadGameFragment));
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            fragmentTransaction.commit();
        }
    };
    GamesDataSourceSingleton gds;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        fm = activity.getFragmentManager();

        ((MainActivity) activity).setGame(null);

        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.action_main);
        container.removeAllViews();

        // New Game Button
        final Button newGameButton = rootView.findViewById(R.id.newGameButton);
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.MainActivity_fragment_container, new MainMenuNewGameFragment());
        fragmentTransaction.addToBackStack(getString(R.string.newGameFragment));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        // Continue Game Button
        final Button continueGameButton = rootView.findViewById(R.id.resumeGameButton);
        gds = GamesDataSourceSingleton.getInstance(activity);
        if (gds.getSavedGames().size() == 0) {
            continueGameButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_disabled));
            continueGameButton.setOnClickListener(null);
        } else {
            continueGameButton.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_fullwidth));
            continueGameButton.setOnClickListener(resumeGameListener);
        }
        return rootView;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void setKeyListenerOnView(View v) {
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.exit)
                            .setMessage(R.string.exitQuestion)
                            .setIcon(android.R.drawable.ic_menu_help)

                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(getString(R.string.no), null)
                            .show();

                    return true;
                } else
                    return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setKeyListenerOnView(getView());
    }
}
