package digitalhealth.de.midsys;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    }

    /**
     * Click listener for the Navigation View menu items
     */
    private class NavigationDrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item){
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.drawer_health:
                    Toast.makeText(thisContext, "Home", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_forum:

                    break;
                case R.id.drawer_tutorial:

                    Toast.makeText(thisContext, "Export success", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.drawer_videos:
                    Toast.makeText(thisContext, "Logout", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.drawer_midwives:
                    Toast.makeText(thisContext, "Logout", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(thisContext, "Invalid Menu", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }
}
