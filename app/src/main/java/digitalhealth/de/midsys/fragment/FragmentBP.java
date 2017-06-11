package digitalhealth.de.midsys.fragment;

import android.widget.ImageView;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 11/06/17.
 */

public class FragmentBP extends FragmentHealthDetails {
    @Override
    protected void updateImage(ImageView imageView){
        imageView.setImageDrawable(getActivity().getDrawable(R.drawable.blood_pressure_chart));
    }
}
