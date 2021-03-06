package map.dtu.f4.sos_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.Victim;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 500);
            return;
        }
        mMap.setMyLocationEnabled(true);
        //LatLng myCoor = (LatLng) getIntent().getSerializableExtra("MyCoor");

        LatLng myCoor = new LatLng(Provider.me.getCoordinate().getLatitude(),Provider.me.getCoordinate().getLongitude());
        setMyLocation(myCoor);

        if(!Provider.listVictim.isEmpty()){
            for (Victim victim : Provider.listVictim){
                setTargetLocations(new LatLng(victim.getCoordinate().getLatitude(),victim.getCoordinate().getLongitude()),victim.getMessage());
            }
        }
    }

    private void setMyLocation(LatLng mycoord){
        //Bitmap icon = resizeMapIcons("myloca",50,50);
        //MarkerOptions mylocation = new MarkerOptions();
        //mylocation.position(mycoord);
        //mylocation.icon(BitmapDescriptorFactory.fromBitmap(icon));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mycoord, 16);
        mMap.animateCamera(cameraUpdate);
        //mMap.addMarker(mylocation);
    }

    private void setTargetLocations(LatLng location,String msg){
            Bitmap icon = resizeMapIcons("targetloca",50,50);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(msg);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            Marker marker =  mMap.addMarker(markerOptions);
            marker.showInfoWindow();

    }

    private Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
