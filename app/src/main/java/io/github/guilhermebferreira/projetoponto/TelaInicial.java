package io.github.guilhermebferreira.projetoponto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static java.lang.String.valueOf;

public class TelaInicial extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {


    private FragmentManager manager = null;
    private FragmentTransaction transaction = null;
    private FragmentConfiguracoes fragmentoConfiguracoes = null;
    private FragmentPonto fragmentoPonto = null;
    private BottomNavigationView menu = null;


    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;

    private static Location localUser;


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        fragmentoConfiguracoes = new FragmentConfiguracoes();
        fragmentoPonto = new FragmentPonto();

        transaction.replace(R.id.TelaPrincipalFrame,fragmentoPonto);
        transaction.commit();

        menu = findViewById(R.id.bottomNavigationView);
        menu.setOnNavigationItemSelectedListener(this);


        new HandleServices(this).execute("http://www.mocky.io/v2/5b5227682e000074005c1c2b");


        // mResultReceiver = new AddressResultReceiver(null);
        // cemented as above explained
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                        @Override
                        public void onSuccess(Location location) {

                            Log.i("INFO", "OnSuccessListenerl onSuccess");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object


                                Log.i("INFO", "Location 1");
                                Log.i("INFO",String.valueOf(location.getLatitude()));
                                Log.i("INFO",String.valueOf(location.getLongitude()));

                                localUser = location;
                            }

                            Log.i("INFO", "Location Null");
                        }
                    });
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data

                        Log.i("INFO", "Location 2");
                        Log.i("INFO",String.valueOf(location.getLatitude()));
                        Log.i("INFO",String.valueOf(location.getLongitude()));

                        localUser = location;
                    }
                }

            };
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        Log.i("INFO", "onNavigationItemSelected");
        switch (item.getItemId()) {
            case R.id.configuracoes:
                Log.i("INFO", "click on CONFIG");

                fragmentoConfiguracoes = new FragmentConfiguracoes();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.TelaPrincipalFrame, fragmentoConfiguracoes);
                //cria um empilhamento de telas, com os fragmentos
                //para o app ter para onde retornar, caso o usuário pressione o botão de voltar
                transaction.addToBackStack("TelaPrincipal");
                transaction.commit();
                break;
            case R.id.ponto:
                Log.i("INFO", "click on PONTO");

                fragmentoPonto = new FragmentPonto();
                transaction = manager.beginTransaction();
                transaction.replace(R.id.TelaPrincipalFrame, fragmentoPonto);

                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", localUser.getLatitude());
                bundle.putDouble("longitude", localUser.getLongitude());
                fragmentoPonto.setArguments(bundle);
                //cria um empilhamento de telas, com os fragmentos
                //para o app ter para onde retornar, caso o usuário pressione o botão de voltar
                transaction.addToBackStack("TelaPrincipal");
                transaction.commit();
                break;
        }

        return true;
    }



    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            startLocationUpdates();
            requestPermissions();
        } else {
            getLastLocation();
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("INFO", "Displaying permission rationale to provide additional context.");



        } else {
            Log.i("INFO", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("INFO", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("INFO", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            }
        }
    }



    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            lastLocation = task.getResult();



                            Log.i("INFO", "Location 3");
                            Log.i("INFO",String.valueOf(lastLocation.getLatitude()));
                            Log.i("INFO",String.valueOf(lastLocation.getLongitude()));


                            localUser = lastLocation;

                        } else {
                            Log.w("INFO", "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }



}
