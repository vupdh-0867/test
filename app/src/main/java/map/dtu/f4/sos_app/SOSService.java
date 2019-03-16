package map.dtu.f4.sos_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View;
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

import map.dtu.f4.sos_app.beans.Contact;
import map.dtu.f4.sos_app.beans.Coordinate;
import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.User;
import map.dtu.f4.sos_app.beans.Victim;

public class SOSService extends Service implements LocationListener{
    LocationManager locationManager;
    DatabaseReference databaseReference;
    private Notification.Builder notBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;

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
        // Thông báo sẽ tự động bị hủy khi người dùng click vào Panel
        this.notBuilder = new Notification.Builder(this);
        this.notBuilder.setAutoCancel(true);
        databaseReference.child("channel").child(Provider.me.getId()).child("victims").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot victim : dataSnapshot.getChildren()) {
                    //Log.d("aaa", victim.toString());
                    //lay trang thai da xem (seen) cua nan nhan
                    //if (victim.child("Seen") != null){
                        String isSeen = victim.child("Seen").getValue().toString();
                        //kiem tra da xem nan nhan chua
                        if (isSeen.equals("false")) {
                            //lay thong tin nan nhan tu Db
                            String victimId = victim.child("VictimId").getValue().toString();
                            String victimName = victim.child("VictimName").getValue().toString();
                            String message = victim.child("VictimMessage").getValue().toString();
                            double victimLatitude = Double.parseDouble(victim.child("VicTimLatitude").getValue().toString());
                            double victimLongitude = Double.parseDouble(victim.child("VicTimLongitude").getValue().toString());
                            long victimTime = Long.parseLong(victim.child("SendTime").getValue().toString());
                            //them nan nhan vao danh sach nan nhan
                            Victim victim1 = new Victim();
                            victim1.setId(victimId);
                            victim1.setName(victimName);
                            victim1.setMessage(message);
                            victim1.setStatus("");
                            Coordinate vicCoordinate = new Coordinate();
                            vicCoordinate.setLatitude(victimLatitude);
                            vicCoordinate.setLongitude(victimLongitude);
                            vicCoordinate.setTime(victimTime);
                            victim1.setCoordinate(vicCoordinate);
                            Provider.listVictim.add(victim1);
                            Toast.makeText(SOSService.this,"SO in danger",Toast.LENGTH_LONG).show();
                            //hien thong bao cau cuu
                            taoThongBao(victim1.getName(),victim1.getMessage());
                            Intent intent = new Intent(SOSService.this, ReceiveSOSActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void taoThongBao(String name, String msg)  {
        // --------------------------
        // Chuẩn bị một thông báo
        // --------------------------
        this.notBuilder.setSmallIcon(R.drawable.myloca);
        this.notBuilder.setTicker("This is a ticker");
        // Sét đặt thời điểm sự kiện xẩy ra.
        // Các thông báo trên Panel được sắp xếp bởi thời gian này.
        this.notBuilder.setWhen(System.currentTimeMillis()+ 10* 1000);
        this.notBuilder.setContentTitle("Thông báo");
        this.notBuilder.setContentText(name+" "+msg);
        // Tạo một Intent
        Intent intent = new Intent(this, ReceiveSOSActivity.class);
        // PendingIntent.getActivity(..) sẽ start mới một Activity và trả về
        // đối tượng PendingIntent.
        // Nó cũng tương đương với gọi Context.startActivity(Intent).
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MY_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.notBuilder.setContentIntent(pendingIntent);
        // Lấy ra dịch vụ thông báo (Một dịch vụ có sẵn của hệ thống).
        NotificationManager notificationService  =
                (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Xây dựng thông báo và gửi nó lên hệ thống.
        Notification notification =  notBuilder.build();
        notificationService.notify(MY_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(getApplicationContext(),"aaa",Toast.LENGTH_LONG).show();
        updateStatus();
        return START_STICKY;

    }
    private void updateLocation(User user){
        DatabaseReference dbUpdate = FirebaseDatabase.getInstance().getReference();
        dbUpdate.child("UserLocation").child(user.getId()).setValue(user);
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
        //Toast.makeText(getApplicationContext(),"coordinate: "+latitude+"-"+longitude,Toast.LENGTH_LONG).show();
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
