package com.ikue.japanesedictionary.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.fragments.FavouritesFragment;
import com.ikue.japanesedictionary.fragments.HistoryFragment;
import com.ikue.japanesedictionary.fragments.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private MenuItem searchMenuItem;
    private FloatingActionButton fabButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // When false, the system sets the default values only if this method has never been called in the past
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);

        // Get the shared preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        appBarLayout = findViewById(R.id.appbar);

        // Add Toolbar to Main Screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Viewpager for tabs
        viewPager = findViewById(R.id.viewpager);
        setupViewPager();

        // Set Tabs inside the Toolbar
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Add icons to each tab
        tabs.getTabAt(0).setIcon(R.drawable.ic_history_white);
        tabs.getTabAt(1).setIcon(R.drawable.ic_home_white);
        tabs.getTabAt(2).setIcon(R.drawable.ic_star_white);

        // Create Navigation drawer and inflate
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer);

        // Add menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behaviour of Navigation drawer
        navigationView.setNavigationItemSelectedListener(item -> {
            // Set item as checked
            item.setChecked(true);

            int itemId = item.getItemId();
            if (itemId == R.id.nav_history_fragment) {
                viewPager.setCurrentItem(0, true);
            } else if (itemId == R.id.nav_home_fragment) {
                viewPager.setCurrentItem(1, true);
            } else if (itemId == R.id.nav_favourites_fragment) {
                viewPager.setCurrentItem(2, true);
            } else if (itemId == R.id.nav_settings_activity) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (itemId == R.id.nav_about_activity) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }

            // Closing drawer on item click
            drawerLayout.closeDrawers();
            return true;
        });

        fabButton = findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates menu and adds to action if present
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnFocusChangeListener((view, queryTextFocused) -> {
            if(!queryTextFocused) {
                // Close the SearchView when the user closes the keyboard
                searchMenuItem.collapseActionView();
            }
        });

        // Set the onClick here so we can guarantee we have a searchMenuItem
        fabButton.setOnClickListener(view -> {
            // Expand the AppBarLayout on click, so SearchView is visible
            appBarLayout.setExpanded(true);

            // Expand the SearchView
            searchMenuItem.expandActionView();
        });

        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        // Set a listener on the SearchView
        searchMenuItem.setOnActionExpandListener(
                new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // When the SearchView is expanded, hide the FAB
                        fabButton.hide();
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Once the SearchView is collapsed, show the FAB again
                        fabButton.show();
                        return true;
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupViewPager() {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.history_fragment_title));
        adapter.addFragment(new HomeFragment(), getString(R.string.home_fragment_title));
        adapter.addFragment(new FavouritesFragment(), getString(R.string.favourites_fragment_title));
        viewPager.setAdapter(adapter);

        // Set default tab to the user's preference. Default is the 'Home' tab.
        viewPager.setCurrentItem(Integer.parseInt(sharedPref.getString("pref_startupPage", "1")));

        // Set the number of pages that should be retained to either side of the current page in
        // the view hierarchy in an idle state.
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When we change tab, set the current tab to be selected in the NavigationDrawer
                navigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
