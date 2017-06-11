package digitalhealth.de.midsys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 11/06/17.
 */

public class FragmentMidwives extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_midwives, container, false);
        return root;
    }
}
