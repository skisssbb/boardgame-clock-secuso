/*
 This file is part of Privacy Friendly App Example.

 Privacy Friendly App Example is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly App Example is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly App Example. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlyboardgameclock.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyboardgameclock.R;

/**
 * @author Christopher Beckmann
 * @version 20161225
 * This activity is an example for the main menu of gaming applications
 */

public class GameActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.scroller);
        if(mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        int index = mSharedPreferences.getInt("lastChosenPage", 0);

        mViewPager.setCurrentItem(index);
        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        //care for initial postiton of the ViewPager
        mArrowLeft.setVisibility((index==0)?View.INVISIBLE:View.VISIBLE);
        mArrowRight.setVisibility((index==mSectionsPagerAdapter.getCount()-1)?View.INVISIBLE:View.VISIBLE);

        //Update ViewPager on change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrowLeft.setVisibility((position==0)?View.INVISIBLE:View.VISIBLE);
                mArrowRight.setVisibility((position==mSectionsPagerAdapter.getCount()-1)?View.INVISIBLE:View.VISIBLE);

                //save position in settings
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("lastChosenPage", position);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_game;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.arrow_left:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.arrow_right:
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
                break;
            default:
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int id = 0;
            if(getArguments() != null) {
                id = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Mode: "+String.valueOf(id));
            return rootView;
        }
    }
}
