package com.example.vikas.wildfireapp;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    double latitude;
    double longitude;
    String TAG = "TAG";
    String provider;
    Criteria locationfind=new Criteria();
    LocationManager locationManager;
    Looper mLooper = Looper.myLooper();
    List<String> matchingProviders = locationManager.getAllProviders();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"ONCREATE");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        setCriteria();
        provider = locationManager.getBestProvider(locationfind,true);
        requestUpdate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    LocationListener listen= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location.getAccuracy()<70) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            else{
                Toast.makeText(getApplicationContext(),"Unable to receive accurate location",Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG,"Location Changed");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "Status Changed");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "Provider Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG,"Provider Disabled");
        }
    };
    protected void onResume(){
        super.onResume();
        refresh();
    }
    //Has higher latency
    //Calls onLocationChanged method in listen (LocationListener)
    private void requestUpdate(){
        locationManager.requestSingleUpdate(provider,listen,mLooper);
    }
    //sets Criteria to find best provider
    private void setCriteria(){
        locationfind.setAccuracy(Criteria.ACCURACY_FINE);
        locationfind.setAltitudeRequired(false);
        locationfind.setBearingRequired(false);
        locationfind.setHorizontalAccuracy(Criteria.ACCURACY_MEDIUM);
        locationfind.setPowerRequirement(Criteria.POWER_MEDIUM);
        locationfind.setSpeedRequired(false);
    }
    //Is a faster way to find a location, but location might not exist to begin with
    private void refresh(){
       Location location = locationManager.getLastKnownLocation(provider);
       setLocation(location);
    }
    private void setLocation(Location location){
        latitude =  location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
