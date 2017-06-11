package digitalhealth.de.midsys;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import digitalhealth.de.midsys.fragment.FragmentForum;
import digitalhealth.de.midsys.fragment.FragmentHealth;
import digitalhealth.de.midsys.fragment.FragmentMidwives;
import digitalhealth.de.midsys.fragment.FragmentTutorial;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getName();

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    private Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        thisContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.snet_app_drawer);

        mToolbar = (Toolbar) findViewById(R.id.snet_main_toolbar);
        setSupportActionBar(mToolbar);
        navigationView = (NavigationView) findViewById(R.id.snet_app_drawerView);
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerItemClickListener());
        navigationView.setBackgroundTintList(null);
        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                mToolbar,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view){
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView){
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = Fragment.instantiate(this, FragmentHealth.class.getName());
        fragmentTransaction.add(R.id.fragmentmain, fragment);
        fragmentTransaction.commit();
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState){
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Click listener for the Navigation View menu items
     */
    private class NavigationDrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            Log.i(TAG, "onNavigationItemSelected");
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            mDrawerLayout.closeDrawers();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.drawer_health:
                    Log.i(TAG, "HEALTH");
                    Fragment fragment1 = Fragment.instantiate(MainActivity.this, FragmentHealth.class.getName());
                    fragmentTransaction.replace(R.id.fragmentmain, fragment1);
                    fragmentTransaction.commit();

                    break;
                case R.id.drawer_forum:
                    Log.i(TAG, "FORUM");
                    Fragment fragment2 = Fragment.instantiate(MainActivity.this, FragmentForum.class.getName());
                    fragmentTransaction.replace(R.id.fragmentmain, fragment2);
                    fragmentTransaction.commit();
                    break;
                case R.id.drawer_tutorial:
                    Log.i(TAG, "TUTORIAL");
                    Fragment fragment3 = Fragment.instantiate(MainActivity.this, FragmentTutorial.class.getName());
                    fragmentTransaction.replace(R.id.fragmentmain, fragment3);
                    fragmentTransaction.commit();

                    break;
                case R.id.drawer_videos:

                    Fragment fragment4 = Fragment.instantiate(MainActivity.this, FragmentTutorial.class.getName());
                    fragmentTransaction.replace(R.id.fragmentmain, fragment4);
                    fragmentTransaction.commit();

                    break;
                case R.id.drawer_midwives:
                    Fragment fragment5 = Fragment.instantiate(MainActivity.this, FragmentMidwives.class.getName());
                    fragmentTransaction.replace(R.id.fragmentmain, fragment5);
                    fragmentTransaction.commit();

                    break;
                default:

                    break;
            }
            return false;
        }
    }

}
