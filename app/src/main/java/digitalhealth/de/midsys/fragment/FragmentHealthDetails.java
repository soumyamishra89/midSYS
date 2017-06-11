package digitalhealth.de.midsys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 10/06/17.
 */

public abstract class FragmentHealthDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_healthdetails, container, false);
        ImageView imageView = (ImageView) root.findViewById(R.id.health_image);
        updateImage(imageView );
        return root;
    }

    protected abstract void updateImage(ImageView imageView );
}
