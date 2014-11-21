package com.example.psn_trophies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.psn_trophies.SearchResultFragment.Scope;

public class BaseActivity extends ActionBarActivity {
    DrawerLayout drawer;
    Toolbar toolbar;
    Menu menu;
    FragmentManager fragmentManager;
    Fragment activeFragment;
    static BaseActivity self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // TOOLBAR STUFF
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // DRAWER STUFF
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        ListView drawerList = (ListView) findViewById(R.id.drawer_left);

        int[] icons = new int[] {
                R.drawable.ic_action_settings,
                R.drawable.ic_action_view_as_list
        };
        String[] captions = new String[] {
                getString(R.string.games),
                getString(R.string.trophies)
        };

        List<HashMap<String, String>> itemList = new ArrayList<>();
        for (int i = 0; i<captions.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("image", Integer.toString(icons[i]));
            map.put("txt", captions[i]);
            itemList.add(map);
        }
        drawerList.setAdapter(new SimpleAdapter(getBaseContext(), itemList, R.layout.drawer_item,
                new String[] {"image", "txt"}, new int[] {R.id.drawer_item_image, R.id.drawer_item_caption}));
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResultFragment games = new SearchResultFragment();
                switch (position) {
                    default:
                    case 0:
                        //if (menu != null) menu.findItem(R.id.action_refresh).setVisible(false);
                        games.setScope(Scope.GAMES);
                        break;
                    case 1:
                        games.setScope(Scope.TROPHIES);
                        break;
                }
                activeFragment = games;
                setContent(activeFragment);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        // CONTENT MANAGEMENT STUFF
        fragmentManager = getSupportFragmentManager();
        activeFragment = new SearchResultFragment();
        fragmentManager.beginTransaction().replace(R.id.content, activeFragment).commit();

        /*fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                // CHANGE WHAT'S INSIDE
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                activeFragment.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setContent(Fragment fragment) {
        self.fragmentManager.beginTransaction().replace(R.id.content, fragment)
            .addToBackStack(null)
            .commit();
    }
}
