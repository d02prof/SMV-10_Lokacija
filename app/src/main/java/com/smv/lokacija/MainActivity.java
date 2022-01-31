package com.smv.lokacija;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;

    private TextView textViewLatitude, textViewLongitude;
    private EditText editTextLocation;
    private Button buttonGetLocation;

    private FusedLocationProviderClient fusedLocationClient;

    View.OnClickListener getLocation = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            GetLocation();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLatitude = findViewById(R.id.textViewLatitude);
        textViewLongitude = findViewById(R.id.textViewLongitude);
        editTextLocation = findViewById(R.id.editTextLocation);
        //editTextLocation.setEnabled(false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        buttonGetLocation = findViewById(R.id.buttonGetLocation);
        buttonGetLocation.setOnClickListener(getLocation);

        GetLocation();

    }


    private void GetLocation(){

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //nimamo dovoljenj
            //hkrati zahtevamo obe dovoljenji
            //fine dela z GPS, coarse dela z WiFijem in mobilnim omrežjem
            //odvisno od nastavitev za zaznavanje lokacije rabimo enega ali drugega
            requestPermissions(new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            Log.d("lokacija_debug", "check Manifest.permission.ACCESS_FINE_LOCATION");

        }
        else{
            //imamo dovoljenji
            Log.d("lokacija_debug", "imamo pravice");
            //getLastLocation vrne zadnjo znano pozicijo, ki jo hrani naprava, kar je običajno tudi trenutna lokacija
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                editTextLocation.setText(location.getLatitude() + ", " + location.getLongitude());
                                textViewLatitude.setText("" + location.getLatitude());
                                textViewLongitude.setText("" + location.getLongitude());
                            }
                        }
                    });
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION ) {
            Log.d("lokacija_debug", "onRequestPermissionsResult: " + requestCode);
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //dobili obe dovoljenji
                Log.d("lokacija_debug", "dobili dovoljenje: " + requestCode);
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                GetLocation();
            } else {
                //nismo dobili dovoljenja
                Toast.makeText(MainActivity.this, "Permission DENIED!", Toast.LENGTH_SHORT).show();
                Log.d("lokacija_debug", "NISMO dobili dovoljenja: " + requestCode);
            }
        }
    }

}






