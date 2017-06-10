package digitalhealth.de.midsys;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import digitalhealth.de.midsys.fragment.FragmentHealth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = MainActivity.class.getName();

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;
    private NavigationView navigationView;

    private Context thisContext;

    ConstraintLayout layoutLocation, layoutStep, layoutArrow, layoutMain, layoutDropdown;
    ImageView imgvArrow;
    FrameLayout layoutFragmentContainer;
    ConstraintSet constraintSet;
    int lastClickSectionId = -1;

    // This hashmap helps recolor the dropdown arrow to match the clicked section
    HashMap<Integer, Integer> colorMap = new HashMap<>();

    // This hashmap will map each section layout with the left most section below it
    // So when the drop down fragment appear we can rearrange the layout constraints
    HashMap<Integer, Integer> viewMap = new HashMap<>();

    // This hashmap maps between section ID and corresponding fragment class
    HashMap<Integer, String> fragmentMap = new HashMap<>();

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



        //Populate colorMap
        colorMap.put(R.id.layoutLocations, R.color.transparentRed);
        colorMap.put(R.id.layoutSteps, R.color.transparentOrange);
        colorMap.put(R.id.layoutMusic, R.color.transparentYellow);
        colorMap.put(R.id.layoutAppUsage, R.color.transparentCyan);
        colorMap.put(R.id.layoutInternetData, R.color.transparentPurple);

        // Populate viewMap
        viewMap.put(R.id.layoutLocations, R.id.layoutMusic);
        viewMap.put(R.id.layoutSteps, R.id.layoutMusic);
        viewMap.put(R.id.layoutMusic, R.id.layoutInternetData);
        viewMap.put(R.id.layoutAppUsage, R.id.layoutInternetData);

        //Populate fragmentMap
        // #TODO implementer of each section fragment need to add an entry to this map
        fragmentMap.put(R.id.layoutLocations, FragmentHealth.class.getName());
        fragmentMap.put(R.id.layoutSteps, FragmentHealth.class.getName());
        fragmentMap.put(R.id.layoutAppUsage, FragmentHealth.class.getName());
        fragmentMap.put(R.id.layoutInternetData, FragmentHealth.class.getName());
        fragmentMap.put(R.id.layoutMusic, FragmentHealth.class.getName());

        layoutLocation = (ConstraintLayout) findViewById(R.id.layoutLocations);

        layoutStep = (ConstraintLayout) findViewById(R.id.layoutSteps);

        layoutDropdown = (ConstraintLayout) findViewById(R.id.layoutFragmentMain);
        layoutFragmentContainer = (FrameLayout) findViewById(R.id.layoutFrameFragment);
        imgvArrow = (ImageView) findViewById(R.id.imgvDownArrow);
        layoutArrow = (ConstraintLayout) findViewById(R.id.layoutArrow);

        layoutMain = (ConstraintLayout) findViewById(R.id.layoutMain);

        constraintSet = new ConstraintSet();
        constraintSet.clone(layoutMain);

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


    @Override
    public void onClick(View v) {
        // If the clicked object is a section, do rearrangement
        int currentClickedViewId = v.getId();

        if (colorMap.containsKey(currentClickedViewId)) {
            // Click on same section again
            if (currentClickedViewId == lastClickSectionId) {
                if (isDropDownFragmentVisible())
                    hideDropDownFragment();
                else {
                    loadNewFragment(currentClickedViewId);
                }
            } else {  // User choose new section
                if (isDropDownFragmentVisible()) {
                    hideDropDownFragment();
                    loadNewFragment(currentClickedViewId);
                } else
                    loadNewFragment(currentClickedViewId);
            }
        } else
            return;
    }

    private void loadNewFragment(int clickedSectionId) {

        // Avoid exception when click on section with unimplemented fragment
        if (!fragmentMap.containsKey(clickedSectionId))
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        //Hide current display fragment
        if(fragmentManager.findFragmentByTag(fragmentMap.get(lastClickSectionId))!= null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(
                    fragmentManager.findFragmentByTag(fragmentMap.get(lastClickSectionId)));
            fragmentTransaction.commit();
        }

        // Load corresponding fragment into framelayout
        String tag = fragmentMap.get(clickedSectionId);
        if(fragmentManager.findFragmentByTag(fragmentMap.get(clickedSectionId))==null) {
            // Each fragment get chosen date in milliseconds from this bundle
            //Bundle fragmentBundle = new Bundle();
            //fragmentBundle.putLong(StaticReferences.BUNDLE_CHOSEN_DATE_KEY, chosenTimestamp);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = Fragment.instantiate(this, tag);
            fragmentTransaction.add(R.id.layoutFrameFragment, fragment, tag);
            fragmentTransaction.commit();
        }
        else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
            fragmentTransaction.commit();
        }

//        Fragment fragment;
//        fragment = Fragment.instantiate(this, fragmentMap.get(clickedSectionId));
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.layoutFrameFragment, fragment);
//        transaction.commit();

        reArrangeConstraints(clickedSectionId);

        imgvArrow.setColorFilter(getResources().getColor(colorMap.get(clickedSectionId)));
        layoutArrow.setVisibility(View.VISIBLE);
        layoutDropdown.setVisibility(View.VISIBLE);
    }

    private void hideDropDownFragment() {
        layoutArrow.setVisibility(View.GONE);
        layoutDropdown.setVisibility(View.GONE);
    }
    private boolean isDropDownFragmentVisible() {
        return (layoutArrow.getVisibility() == View.VISIBLE);
    }

    private void reArrangeConstraints(int clickedSectionId) {
        // If the user clicks on different section, need to restore the old constraints first
        if (clickedSectionId != lastClickSectionId && lastClickSectionId != -1 && viewMap.containsKey(lastClickSectionId)) {
            constraintSet.connect(viewMap.get(lastClickSectionId),
                    ConstraintSet.TOP, lastClickSectionId, ConstraintSet.BOTTOM);
        }
        lastClickSectionId = clickedSectionId;

        constraintSet.connect(layoutArrow.getId(), ConstraintSet.TOP, clickedSectionId, ConstraintSet.BOTTOM);
        constraintSet.connect(layoutArrow.getId(), ConstraintSet.LEFT, clickedSectionId, ConstraintSet.LEFT);
        constraintSet.connect(layoutArrow.getId(), ConstraintSet.RIGHT, clickedSectionId, ConstraintSet.RIGHT);
        // Last row sections don't need this constraints
        if (viewMap.containsKey(clickedSectionId))
            constraintSet.connect(viewMap.get(clickedSectionId),
                    ConstraintSet.TOP, layoutDropdown.getId(), ConstraintSet.BOTTOM);
        constraintSet.applyTo(layoutMain);
    }

}
