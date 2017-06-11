package digitalhealth.de.midsys.fragment;

import android.widget.ImageView;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 11/06/17.
 */

public class FragmentSleep extends FragmentHealthDetails {
    @Override
    protected void updateImage(ImageView imageView){
        imageView.setImageDrawable(getActivity().getDrawable(R.drawable.sleep));
    }
}
