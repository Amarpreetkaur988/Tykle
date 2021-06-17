package com.tykle.Classess;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

public class LocationActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    public static String lati, longi;

    public String TAG = "LocationActivity";
    public long INTERVAL = 1000 * 10;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String mLastUpdateTime;
    Context context;
    Boolean boolOneTime = false;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;

    public LocationActivity(Context mContext, Boolean boolOneTime)
    {
        this.context = mContext;
        this.boolOneTime = boolOneTime;

    }

    @SuppressLint("RestrictedApi")
    public void createLocationRequest()
    {
        try {
            if(mLocationRequest == null) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval( INTERVAL );
                mLocationRequest.setSmallestDisplacement( 80 );
                mLocationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
            }
        } catch(Exception e) {
            // TODO: handle exception
        }

    }

    public void getLocation()
    {
        try {
            Log.e( "LocationActivity", "Get Location Methos" );
            if(mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder( context )
                        .addApi( LocationServices.API )
                        .addConnectionCallbacks( this )

                        // .enableAutoManage((FragmentActivity) context /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .addOnConnectionFailedListener( this )
                        .build();
            }
            if(!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        } catch(Exception e) {
            // TODO: handle exception.
            e.printStackTrace();
        }

        Log.d( TAG, "onCreate ..............................." );
        //show error dialog if GoolglePlayServices not available
        if(isGooglePlayServicesAvailable()) {
            createLocationRequest();
        }
    }


    private boolean isGooglePlayServicesAvailable()
    {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( context );
        if(ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog( status, (Activity) context, 0 ).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.d( TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected() );
        preferences = PreferenceManager.getDefaultSharedPreferences( context );
        editor = preferences.edit();
        startLocationUpdates();
    }

    protected void startLocationUpdates()
    {

        try {


            if(ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );

            if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
                // CommonUtils. buildAlertMessageNoGps(context);
            } else {

                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d( TAG, "Location update started ..............: " );
    }


    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.d( TAG, "Connection failed: " + connectionResult.toString() );
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d( TAG, "Firing onLocationChanged.............................................." );
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format( new Date() );
        updateUI();
    }

    private void updateUI()
    {
        Log.d( TAG, "UI update initiated ............." );
        if(null != mCurrentLocation) {
            String lat = String.valueOf( mCurrentLocation.getLatitude() );
            String lng = String.valueOf( mCurrentLocation.getLongitude() );
            Log.e( "At Time: ", "" + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider() );

            HelperClass.setLatitude( String.valueOf( mCurrentLocation.getLatitude() ), (Activity) context );
            HelperClass.setLongitude( String.valueOf( mCurrentLocation.getLongitude() ), (Activity) context );
            Log.e( "LocationActivity", "Latitude" + HelperClass.getLatitude( (Activity) context ) );
            Log.e( "LocationActivity", "Longitude" + HelperClass.getLongitude( (Activity) context ) );

            try {
              /*  SharedPrefrences.set_current_lat((Activity) context,Globals.currentlatlng.latitude+"");
                SharedPrefrences.set_current_long((Activity) context,Globals.currentlatlng.longitude+"");*/
            } catch(Exception e) {
                e.printStackTrace();
            }
//           Globals.currentlatlng=new LatLng(40.8154,-73.0456);

            if(boolOneTime) {
//                UpdateLatlng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                stopLocationUpdates();
            }
        } else {
            Log.d( TAG, "location is null ..............." );
        }
    }


    public void stopLocationUpdates()
    {
        try {
            if(mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
//        Globals.mGoogleApiClient.disconnect();
        Log.d( TAG, "Location update stopped ......................." );
    }


}