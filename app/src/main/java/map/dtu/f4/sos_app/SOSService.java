package map.dtu.f4.sos_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import map.dtu.f4.sos_app.beans.Coordinate;
import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.User;

public class SOSService extends Service implements LocationListener{
    LocationManager locationManager;
    DatabaseReference databaseReference;
    String myStatus = "1";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates("gps",1000,0,this);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String userId = Provider.me.getId();
//                for(DataSnapshot user : dataSnapshot.child("UserLocation").getChildren())
//                {
//                    String id = user.getKey().toString();
//                    if(!userId.equals(id)) {
//                        double latitude = Double.parseDouble(user.child("coordinate").child("latitude").getValue().toString());
//                        double longitude = Double.parseDouble(user.child("coordinate").child("longitude").getValue().toString());
//                        String status = user.child("status").getValue().toString();
//                        double distance = Math.sqrt(Math.pow(Provider.me.getCoordinate().getLatitude()-latitude,2)+Math.pow(Provider.me.getCoordinate().getLongitude()-longitude,2));
//                        ArrayList<LatLng> vicCor = new ArrayList<>();
//                        vicCor.add(new LatLng(Provider.me.getCoordinate().getLatitude(),Provider.me.getCoordinate().getLongitude()));
//                        if((distance<(4.5*Math.pow(10,-3)))&myStatus.equals("1")&status.equals("3")) {
//                            if(!checkInContact(dataSnapshot.child("InContact"),userId,id)) {
//                                Toast.makeText(getApplicationContext(), "so is in danger", Toast.LENGTH_LONG).show();
//                                vicCor.add(new LatLng(latitude, longitude));
//                                Intent intent = new Intent(SOSService.this, ReceiveSOSActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra("MyID", userId);
//                                intent.putExtra("VictimID", id);
//                                intent.putExtra("VictimCoor", vicCor);
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.child("channel").child(Provider.me.getId()).child("victims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"aaa",Toast.LENGTH_LONG).show();
        updateStatus();
        return START_STICKY;

    }
    private void updateLocation(User user){
        databaseReference.child("UserLocation").child(user.getId()).setValue(user);
    }
    //ham cap nhat thong tin len db moi 10s
    private void updateStatus(){
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Date date;
                date = new Date();
                long time = date.getTime();
                Provider.me.getCoordinate().setTime(time);
                updateLocation(Provider.me);
            }
        };
        Timer t = new Timer();
        t.schedule(timerTask, 3,10000);// 10000 tuong ung voi 10 s
    }
    @Override
    public void onLocationChanged(Location location) {
        double latitude = (double) location.getLatitude();
        double longitude = (double) location.getLongitude();
        //set thong tin ve toa do hien tai của nguoi dung
        Provider.me.getCoordinate().setLongitude(longitude);
        Provider.me.getCoordinate().setLatitude(latitude);
        Toast.makeText(getApplicationContext(),"coordinate: "+latitude+"-"+longitude,Toast.LENGTH_LONG).show();
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
}
