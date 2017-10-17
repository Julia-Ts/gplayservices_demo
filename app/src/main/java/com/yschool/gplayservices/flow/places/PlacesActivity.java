package com.yschool.gplayservices.flow.places;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yschool.gplayservices.R;
import com.yschool.gplayservices.base.BaseMvpActivity;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Created by jtsym on 10/16/2017.
 */

public class PlacesActivity extends BaseMvpActivity<PlacesContract.Presenter> implements PlacesContract.View,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {

    public static int REQUEST_LOCATION_PERMISSIONS = 9123;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @BindView(R.id.likely_place)
    TextView likelyPlaceView;

    private GoogleApiClient googleApiClient;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPlayServices()) {
            setupGoogleApiClient();
            setupMap();
        }
    }

    @NonNull
    @Override
    protected PlacesContract.Presenter getPresenterInstance() {
        return new PlacesPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_places;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    private void setupGoogleApiClient() {
        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guessCurrentPlace();
            } else {
                permissionsDenied();
            }
        }
    }

    private void permissionsDenied() {
        Toast.makeText(this, getString(R.string.you_shall_not_pass), Toast.LENGTH_LONG).show();
    }

    private void guessCurrentPlace() {
        if (checkPermission()) {
            map.setMyLocationEnabled(true);
        } else {
            askPermission();
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {

            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                if (likelyPlaces.getCount() > 0) {
                    PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                    String content = "";
                    if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {
                        com.google.android.gms.location.places.Place likelyPlace = placeLikelihood.getPlace();
                        String likelyPlaceName = likelyPlace.getName().toString();
                        content = getString(R.string.most_likely_place_template, likelyPlaceName);
                        moveCameraToPlace(likelyPlace.getLatLng(), likelyPlaceName);
                    } else if (placeLikelihood != null) {
                        content += getString(R.string.percent_chance_of_being_here_template, placeLikelihood.getLikelihood() * 100);
                    }
                    likelyPlaceView.setText(content);
                    likelyPlaces.release();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void moveCameraToPlace(LatLng coordinates, String markerTitle) {
        map.addMarker(new MarkerOptions().position(coordinates).title(markerTitle));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    protected boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Timber.i("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        guessCurrentPlace();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //ignore
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(PlacesActivity.class.getSimpleName(), getString(R.string.connection_failed));
    }

}
