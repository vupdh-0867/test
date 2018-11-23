package map.dtu.f4.sos_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import map.dtu.f4.sos_app.beans.Coordinate;
import map.dtu.f4.sos_app.beans.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener {
    LocationManager locationManager;
    DatabaseReference databaseReference;
    User user;
    Coordinate coordinate;
    String myStatus = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = new User();
        coordinate = new Coordinate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates("gps",1000,0,this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void updateLocation(User user){
        databaseReference.child("UserLocation").child(user.getId()).setValue(user);
    }
    @Override
    public void onLocationChanged(Location location) {
//        SharedPreferences sharedPreferences = getSharedPreferences("userInfor",Context.MODE_PRIVATE);
//        String id = sharedPreferences.getString("id","null");
//        String name = "vu";
//        double latitude = (double) location.getLatitude();
//        double longitude = (double) location.getLongitude();
//        Date date;
//        date = new Date();
//        long time = date.getTime();
//        coordinate.setLatitude(latitude);
//        coordinate.setLongitude(longitude);
//        coordinate.setTime(time);
//        user.setId(id);
//        user.setName(name);
//        user.setCoordinate(coordinate);
//        user.setStatus(myStatus);
//        updateLocation(user);
//        Toast.makeText(MainActivity.this,"coordinate: "+latitude+"-"+longitude,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        databaseReference.child("UserLocation").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                SharedPreferences sharedPreferences = getSharedPreferences("userInfor",Context.MODE_PRIVATE);
//                String userId = sharedPreferences.getString("id","null");
//                for(DataSnapshot user : dataSnapshot.getChildren())
//                {
//                    String id = user.getKey().toString();
//                    if(!userId.equals(id)) {
//                        double latitude = Double.parseDouble(user.child("coordinate").child("latitude").getValue().toString());
//                        double longitude = Double.parseDouble(user.child("coordinate").child("longitude").getValue().toString());
//                        String status = user.child("status").getValue().toString();
//                        double distance = Math.sqrt(Math.pow(coordinate.getLatitude()-latitude,2)+Math.pow(coordinate.getLongitude()-longitude,2));
//                        ArrayList<LatLng> vicCor = new ArrayList<>();
//                        vicCor.add(new LatLng(coordinate.getLatitude(),coordinate.getLongitude()));
//                        if((distance<(4.5*Math.pow(10,-3)))&myStatus.equals("1")&status.equals("3")) {
//                            Toast.makeText(MainActivity.this, "so is in danger", Toast.LENGTH_LONG).show();
//                            vicCor.add(new LatLng(latitude,longitude));
//                            Intent intent = new Intent(getBaseContext(), ReceiveSOSActivity.class);
//                            intent.putExtra("VictimCoor",vicCor);
//                            startActivity(intent);
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            SharedPreferences sharedPreferences = getSharedPreferences("userInfor",Context.MODE_PRIVATE);
            String id = sharedPreferences.getString("id","null");
            if(myStatus.equals("1")){
                databaseReference.child("UserLocation").child(id).child("status").setValue("3");
                databaseReference.child("UserLocation").child(id).child("message").setValue("Đang gặp nguy hiểm!");
                //databaseReference.child("InContact").setValue(id);
                myStatus = "3";
            }
            else{
                databaseReference.child("UserLocation").child(id).child("status").setValue("1");
                databaseReference.child("UserLocation").child(id).child("message").setValue("null");
                databaseReference.child("InContact").child(id).removeValue();
                myStatus = "1";
            }
            Toast.makeText(MainActivity.this,"oke"+myStatus.toString(),Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }
}
