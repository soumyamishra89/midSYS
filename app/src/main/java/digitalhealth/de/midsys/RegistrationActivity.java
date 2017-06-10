package digitalhealth.de.midsys;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    String callerType = "Learner";

    Button registerButton;

    private final Integer LOCATION_REQUEST = 1;

    private final String TAG = RegistrationActivity.class.getName();

    private TextView countryName;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        countryName = (TextView) findViewById(R.id.country_name);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String caller = sharedPref.getString("callerType", "");
        if(!caller.isEmpty()) {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogrp);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                if (radioGroup.getChildAt(i) instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if(radioButton.getText().toString().equals(caller)){
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }

        if(!checkOrRequestUserPermission(Manifest.permission.ACCESS_COARSE_LOCATION) || !checkOrRequestUserPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            String country_name = null;
            LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Geocoder geocoder = new Geocoder(getApplicationContext());
            for(String provider: lm.getAllProviders()) {
                @SuppressWarnings ("ResourceType") Location location = lm.getLastKnownLocation(provider);
                if(location!=null) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if(addresses != null && addresses.size() > 0) {
                            country_name = addresses.get(0).getCountryName();
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            countryName.setText(country_name);
        }
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(registerClickListener);
        String[] array = {"Arabic", "Dari", "English",  "Parsi",  "Somali", "Dutch", "Danish", "Finnish", "German", "Portuguese", "French", "Italian", "Polish", "Spanish"};
        List<String> languages = Arrays.asList(array);
        Collections.sort(languages, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs){
                return lhs.compareTo(rhs);
            }
        });


    }

    Button.OnClickListener registerClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v){
        }

    };

    public void onRadioButtonClicked(View view){
        RadioButton radioButton = (RadioButton) view;
        Boolean checked = radioButton.isChecked();
        switch (radioButton.getText().toString()) {
            case "Learner":
                if (checked) {
                    callerType = "Learner";
                    registerButton.setEnabled(true);
                }
                break;
            case "Native":
                if (checked) {
                    callerType = "Native";
                    registerButton.setEnabled(true);
                }
                break;
        }
    }

    private boolean checkOrRequestUserPermission(String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }

        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST){
            boolean permissionGranted = true;
            for(int i=0; i<grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionGranted = false;
                    break;
                }
            }

            if(permissionGranted){
                String country_name = null;
                LocationManager lm = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                for(String provider: lm.getAllProviders()) {
                    @SuppressWarnings ("ResourceType") Location location = lm.getLastKnownLocation(provider);
                    if(location!=null) {
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if(addresses != null && addresses.size() > 0) {
                                country_name = addresses.get(0).getCountryName();
                                break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                countryName.setText(country_name);
            }
        }
    }
}
