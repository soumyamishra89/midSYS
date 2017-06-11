package digitalhealth.de.midsys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.HashMap;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 11/06/17.
 */

public class FragmentHealth extends Fragment implements View.OnClickListener {


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Log.i("FragmentHealth", "onCreateView");

        View root = inflater.inflate(R.layout.fragment_health, container, false);

        //Populate colorMap
        colorMap.put(R.id.layoutBP, R.color.transparentRed);
        colorMap.put(R.id.layoutSleep, R.color.transparentOrange);
        colorMap.put(R.id.layoutHeartRate, R.color.transparentYellow);
        colorMap.put(R.id.layoutProtein, R.color.transparentCyan);


        // Populate viewMap
        viewMap.put(R.id.layoutBP, R.id.layoutHeartRate);
        viewMap.put(R.id.layoutSleep, R.id.layoutHeartRate);
        /*viewMap.put(R.id.layoutHeartRate, R.id.layoutInternetData);
        viewMap.put(R.id.layoutHeartRate, R.id.layoutInternetData);
*/
        //Populate fragmentMap
        fragmentMap.put(R.id.layoutBP, FragmentBP.class.getName());
        fragmentMap.put(R.id.layoutSleep, FragmentSleep.class.getName());
        fragmentMap.put(R.id.layoutHeartRate, FragmentHeartRate.class.getName());

        layoutLocation = (ConstraintLayout) root.findViewById(R.id.layoutBP);

        layoutStep = (ConstraintLayout) root.findViewById(R.id.layoutSleep);

        layoutDropdown = (ConstraintLayout) root.findViewById(R.id.layoutFragmentMain);
        layoutFragmentContainer = (FrameLayout) root.findViewById(R.id.layoutFrameFragment);
        imgvArrow = (ImageView) root.findViewById(R.id.imgvDownArrow);
        layoutArrow = (ConstraintLayout) root.findViewById(R.id.layoutArrow);

        layoutMain = (ConstraintLayout) root.findViewById(R.id.layoutMain);

        root.findViewById(R.id.layoutWeather).setOnClickListener(this);
        root.findViewById(R.id.layoutBP).setOnClickListener(this);
        root.findViewById(R.id.layoutSleep).setOnClickListener(this);
        root.findViewById(R.id.layoutHeartRate).setOnClickListener(this);
        root.findViewById(R.id.layoutProtein).setOnClickListener(this);

        constraintSet = new ConstraintSet();
        constraintSet.clone(layoutMain);
        return root;
    }




    @Override
    public void onClick(View v){
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

    private void loadNewFragment(int clickedSectionId){

        // Avoid exception when click on section with unimplemented fragment
        if (!fragmentMap.containsKey(clickedSectionId))
            return;

        FragmentManager fragmentManager = getChildFragmentManager();

        //Hide current display fragment
        if (fragmentManager.findFragmentByTag(fragmentMap.get(lastClickSectionId)) != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(
                    fragmentManager.findFragmentByTag(fragmentMap.get(lastClickSectionId)));
            fragmentTransaction.commit();
        }

        // Load corresponding fragment into framelayout
        String tag = fragmentMap.get(clickedSectionId);
        if (fragmentManager.findFragmentByTag(fragmentMap.get(clickedSectionId)) == null) {
            // Each fragment get chosen date in milliseconds from this bundle
            //Bundle fragmentBundle = new Bundle();
            //fragmentBundle.putLong(StaticReferences.BUNDLE_CHOSEN_DATE_KEY, chosenTimestamp);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = Fragment.instantiate(getActivity(), tag);
            fragmentTransaction.add(R.id.layoutFrameFragment, fragment, tag);
            fragmentTransaction.commit();
        } else {
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

    private void hideDropDownFragment(){
        layoutArrow.setVisibility(View.GONE);
        layoutDropdown.setVisibility(View.GONE);
    }

    private boolean isDropDownFragmentVisible(){
        return ( layoutArrow.getVisibility() == View.VISIBLE );
    }

    private void reArrangeConstraints(int clickedSectionId){
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
