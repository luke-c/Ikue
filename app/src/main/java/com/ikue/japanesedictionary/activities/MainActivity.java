package com.ikue.japanesedictionary.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    private SearchView searchView;
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

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        // Add Toolbar to Main Screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Viewpager for tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        // Set Tabs inside the Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Add icons to each tab
        tabs.getTabAt(0).setIcon(R.drawable.ic_history_white);
        tabs.getTabAt(1).setIcon(R.drawable.ic_home_white);
        tabs.getTabAt(2).setIcon(R.drawable.ic_star_white);

        // Create Navigation drawer and inflate
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Add menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behaviour of Navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Set item as checked
                item.setChecked(true);

                switch(item.getItemId()) {
                    case R.id.nav_history_fragment:
                        viewPager.setCurrentItem(0, true);
                        break;
                    case R.id.nav_home_fragment:
                        viewPager.setCurrentItem(1, true);
                        break;
                    case R.id.nav_favourites_fragment:
                        viewPager.setCurrentItem(2, true);
                        break;
                    case R.id.nav_settings_activity:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        break;
                    case R.id.nav_about_activity:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;
                    default:
                        break;
                }

                // Closing drawer on item click
                drawerLayout.closeDrawers();
                return true;
            }
        });

        fabButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates menu and adds to action if present
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    // Close the SearchView when the user closes the keyboard
                    MenuItemCompat.collapseActionView(searchMenuItem);
                }
            }
        });

        // Set the onClick here so we can guarantee we have a searchMenuItem
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Expand the AppBarLayout on click, so SearchView is visible
                appBarLayout.setExpanded(true);

                // Expand the SearchView
                MenuItemCompat.expandActionView(searchMenuItem);
            }
        });

        ComponentName cn = new ComponentName(this, SearchResultActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        // Set a listener on the SearchView
        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
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
