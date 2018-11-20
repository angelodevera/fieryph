package ricamae.gomez.devera.fieryph;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;
    LocationManager locationManager2;
    LocationListener locationListener2;
    TextView loc;
    GoogleMap gmap;
    private MapView mapView;
    String stringifyloc;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.i("4itg","teste0");
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.i("4itg","teste1");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                Log.i("4itg","testee");

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, locationListener);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        loc = findViewById(R.id.location);
        loc.setText("  Getting your location...");
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                Log.i("4itg", location.toString());

                if(gmap!=null){
                    LatLng lats = new LatLng(location.getLatitude(),location.getLongitude());
                    gmap.addMarker(new MarkerOptions().position(lats).title("Your Here"));
                    gmap.moveCamera(CameraUpdateFactory.newLatLng(lats));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                            .zoom(17)                   // Sets the zoom
                            .bearing(90)                // Sets the orientation of the camera to east
                            .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(listAddresses!=null && listAddresses.size() > 0){
                        Address address = listAddresses.get(0);
                        Log.i("4itgloc",listAddresses.get(0).toString());
                        String stringaddress = address.getAddressLine(0) +  " ," + (address.getSubAdminArea()!=null? address.getSubAdminArea(): "") + " " + (address.getLocality() !=null? address.getLocality() : "")+ " " + (address.getPostalCode() !=null? address.getPostalCode() : "");
                        loc.setText(stringaddress);
                        stringifyloc = stringaddress;
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, locationListener);
        }
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                Log.i("4itg", "testee");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 0, locationListener);
            }
        }
    }
    public void homeback (View v){
        super.onBackPressed();
    }
    public void call(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + "911"));
        startActivity(intent);
    }

    public void messaging (View v){
        if(stringifyloc!=null){

            String loc = "TxtFire: Reporting fire here at " + stringifyloc;
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:"));
            sendIntent.putExtra("sms_body", loc);
            sendIntent.setType("vnd.android-dir/mms-sms");
            sendIntent.putExtra("address", "09959312345"); // add textfire
            startActivity(sendIntent);
        }
        else{
            Toast.makeText(this, "Location not yet found please wait...", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gmap = map;
//        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
