package map.dtu.f4.sos_app;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import map.dtu.f4.sos_app.beans.Provider;
import map.dtu.f4.sos_app.beans.User;

public class HomeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //khoi tao chanel neu chua co
        databaseReference.child("channel").child(Provider.me.getId()).child("status").setValue("ok");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //su kien giu nut giam am luong
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        databaseReference.child("UserLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("vuPhan",Provider.me.getCoordinate().getLatitude()+"____"+Provider.me.getCoordinate().getLongitude());
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    //tim nguoi xung quanh 100m
                    User me = Provider.me;
                    String id = user.getKey().toString();
                    double latitude = Double.parseDouble(user.child("coordinate").child("latitude").getValue().toString());
                    double longitude = Double.parseDouble(user.child("coordinate").child("longitude").getValue().toString());
                    double distance = Math.sqrt(Math.pow(me.getCoordinate().getLatitude()-latitude,2)+Math.pow(me.getCoordinate().getLongitude()-longitude,2));
                    Log.d("vuPhan",distance+"");
                    //neu khoang cach < 100 va khong phai minh
                    if(distance<(4.5*Math.pow(10,-3))&!id.equals(me.getId())){
                        databaseReference.child("channel").child(id).child("victims").child(me.getId()).setValue(me);
                        databaseReference.child("channel").child(id).child("victims").child(me.getId()).child("seen").setValue("0");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return super.onKeyLongPress(keyCode, event);
    }

}
