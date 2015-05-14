package onsankawai.feedrdr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.common.api.GoogleApiClient;

import origamilabs.library.views.StaggeredGridView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static final String USER_PARCEL = "user_parcel";
    public static Context ApplicationContext;
    private AppUser currentUser;
    private boolean isSignedIn = false;
    private MenuItem signInItem;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.RC_SIGN_IN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                isSignedIn = true;
                currentUser = (AppUser)data.getParcelableExtra(USER_PARCEL);
                mNavigationDrawerFragment.setUserParameters(currentUser.getDisplayName(),currentUser.getProfileImgUrl());
                invalidateOptionsMenu();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationContext = getApplicationContext();
        mTitle = getString(R.string.title_section1);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isSignedIn) {
            signInItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            signInItem = menu.getItem(1);

            // Get the SearchView and set the searchable configuration
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            // Assumes current activity is the searchable activity
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default



            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0) {
            restoreActionBar();
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void startSignIn(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent,LoginActivity.RC_SIGN_IN);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private FragmentManager fragmentManager;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            fragmentManager = getActivity().getSupportFragmentManager();

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final StaggeredGridView gridView = (StaggeredGridView) rootView.findViewById(R.id.staggeredGridView1);
            final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();

           // int margin = getResources().getDimensionPixelSize(R.dimen.margin);
            int margin = 10;
            gridView.setItemMargin(margin); // set the GridView margin

            gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well

            StaggeredAdapter adapter = new StaggeredAdapter(this.getActivity(), R.layout.grid_staggered);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new StaggeredGridView.OnItemClickListener() {
                @Override
                public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
                    NewsFeed feed = (NewsFeed)gridView.getAdapter().getItem(position);
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setTitle(feed.getTitle());

                    NewsFeedFragment fragment = new NewsFeedFragment();
                    Bundle args = new Bundle();
                    args.putString(NewsFeedFragment.ARG_FEED_URL, feed.getSourceUrl());
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            adapter.switchSection(getArguments().getInt(ARG_SECTION_NUMBER));

            //adapter.notifyDataSetChanged();

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
